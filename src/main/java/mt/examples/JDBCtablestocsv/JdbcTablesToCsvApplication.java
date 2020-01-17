package mt.examples.JDBCtablestocsv;

import lombok.SneakyThrows;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.hibernate.internal.SessionImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootApplication
public class JdbcTablesToCsvApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(JdbcTablesToCsvApplication.class, args);
	}

	@PersistenceContext
	EntityManager em;

	@Override
	@Transactional
	public void run(String... args) throws Exception {
		Connection connection = getConnection(em);
		List<String> tableNames = getTableNames(connection, "admin%");

		tableNames.stream()
				.peek(tableName -> System.out.println("Reading table name " + tableName))
				.map(tableName -> readTable(connection, tableName))
				.forEach(this::writeToCsv);
	}

	private class TableRepresentation{
		String tableName;
		List<String> columName;
		List<List<String>> rows;

		public TableRepresentation(String tableName, List<String> columName, List<List<String>> rows) {
			this.tableName = tableName;
			this.columName = columName;
			this.rows = rows;
		}
	}
	@SneakyThrows
	private void writeToCsv(TableRepresentation tableRepresentation){
		try (CSVPrinter printer = createCsvPrinter(tableRepresentation)) {
			tableRepresentation.rows.stream()
					.forEach(row -> printRecord(printer, row.toArray(new String[1])));
		}
	}

	private CSVPrinter createCsvPrinter(TableRepresentation tableRepresentation) throws IOException {
		File directory = new File("./csv");
		if (!directory.exists()){
			directory.mkdirs();
		}
		File csvFile = new File("./csv/" +tableRepresentation.tableName + ".csv");
		return new CSVPrinter(new FileWriter(csvFile), CSVFormat.DEFAULT.withHeader(tableRepresentation.columName.toArray(new String[1])));
	}

	@SneakyThrows
	private TableRepresentation readTable(Connection connection, String tableName) {
		PreparedStatement ps = connection.prepareStatement("select * from " + tableName);
		return new TableRepresentation(tableName, getColumnNames(ps), getRows(ps));
	}
	@SneakyThrows
	private void printRecord(CSVPrinter printer, String... values) {
		printer.printRecord(values);
	}

	private List<String> getColumnNames(PreparedStatement ps) throws SQLException {
		List<String> columnNames = new ArrayList<>();
		for (int i=1; i<=ps.getMetaData().getColumnCount(); i++){
			columnNames.add(ps.getMetaData().getColumnName(i));
		}
		return columnNames;
	}
	private List<List<String>> getRows(PreparedStatement ps) throws SQLException {
		ResultSet resultSet = ps.executeQuery();
		List<List<String>> lines = new ArrayList<>();
		while (resultSet.next()){
			lines.add(getResultSetLine(resultSet));
		}
		return lines;
	}
	private List<String> getResultSetLine(ResultSet resultSet) throws SQLException {
		List<String> lineItems = new ArrayList<>();
		for (int i=1; i<=resultSet.getMetaData().getColumnCount(); i++){
			lineItems.add(Optional.ofNullable(resultSet.getObject(i)).map(String::valueOf).orElse(""));
		}
		return lineItems;
	}

	private List<String> getTableNames(Connection connection, String tableNamePattern) throws SQLException {
		List<String> tableNames = new ArrayList<>();
		DatabaseMetaData dbmd = connection.getMetaData();
		String[] types = {"TABLE"};
		ResultSet rs = dbmd.getTables(null, null, tableNamePattern, types);
		while (rs.next()) {
			String tableName = rs.getString("TABLE_NAME");
			tableNames.add(tableName);
		}
		return tableNames;
	}

	private Connection getConnection(EntityManager em) {
		return em.unwrap(SessionImpl.class).connection();
	}
}
