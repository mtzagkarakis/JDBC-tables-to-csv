package mt.examples.JDBCtablestocsv;

import mt.examples.JDBCtablestocsv.application.FileWriter;
import mt.examples.JDBCtablestocsv.application.JdbcDbMetadataReader;
import mt.examples.JDBCtablestocsv.application.TableReader;
import mt.examples.JDBCtablestocsv.application.WritingAlgo;
import mt.examples.JDBCtablestocsv.application.csv.CsvFilePerTableFileWriter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JdbcTablesToCsvApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(JdbcTablesToCsvApplication.class, args);
	}



	private JdbcDbMetadataReader jdbcDbMetadataReader;
	private TableReader tableReader;
	private WritingAlgo writingAlgo;

	public JdbcTablesToCsvApplication(JdbcDbMetadataReader jdbcDbMetadataReader, TableReader tableReader, WritingAlgo writingAlgo) {
		this.jdbcDbMetadataReader = jdbcDbMetadataReader;
		this.tableReader = tableReader;
		this.writingAlgo = writingAlgo;
	}

	@Override
	public void run(String... args) throws Exception {
		FileWriter fileWriter = new CsvFilePerTableFileWriter("./csv");
		writingAlgo.run(jdbcDbMetadataReader, tableReader, fileWriter, "admin%");
	}
}
