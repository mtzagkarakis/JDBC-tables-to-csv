package mt.examples.JDBCtablestocsv;

import mt.examples.JDBCtablestocsv.application.CsvWriter;
import mt.examples.JDBCtablestocsv.application.JdbcDbMetadataReader;
import mt.examples.JDBCtablestocsv.application.TableReader;
import org.hibernate.internal.SessionImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Connection;
import java.util.List;

@SpringBootApplication
public class JdbcTablesToCsvApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(JdbcTablesToCsvApplication.class, args);
	}

	@PersistenceContext
	EntityManager em;

	private JdbcDbMetadataReader jdbcDbMetadataReader;
	private TableReader tableReader;
	private CsvWriter csvWriter;

	public JdbcTablesToCsvApplication(JdbcDbMetadataReader jdbcDbMetadataReader, TableReader tableReader, CsvWriter csvWriter) {
		this.jdbcDbMetadataReader = jdbcDbMetadataReader;
		this.tableReader = tableReader;
		this.csvWriter = csvWriter;
	}

	@Override
	@Transactional
	public void run(String... args) throws Exception {
		Connection connection = em.unwrap(SessionImpl.class).connection();
		List<String> tableNames = jdbcDbMetadataReader.getTableNames(connection, "admin%");

		tableNames.stream()
				.peek(tableName -> System.out.println("Reading table name " + tableName))
				.map(tableName -> tableReader.readTable(connection, tableName))
				.forEach(csvWriter::writeToCsv);
	}
}
