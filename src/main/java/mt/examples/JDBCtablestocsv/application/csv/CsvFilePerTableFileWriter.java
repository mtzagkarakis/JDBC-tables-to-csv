package mt.examples.JDBCtablestocsv.application.csv;

import lombok.SneakyThrows;
import mt.examples.JDBCtablestocsv.application.FileWriter;
import mt.examples.JDBCtablestocsv.domain.TableRepresentation;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.File;

public class CsvFilePerTableFileWriter implements FileWriter {
    private final String directoryPathName;

    public CsvFilePerTableFileWriter(String directoryPathName) {
        this.directoryPathName = directoryPathName;
    }

    @Override
    @SneakyThrows
    public void writeToFile(TableRepresentation tableRepresentation) {
        try (CSVPrinter printer = createCsvPrinter(tableRepresentation)) {
            tableRepresentation.getRows().stream()
                    .forEach(row -> printRecord(printer, row.toArray(new String[1])));
        }
    }

    @SneakyThrows
    private CSVPrinter createCsvPrinter(TableRepresentation tableRepresentation) {
        File directory = new File(directoryPathName);
        if (!directory.exists()){
            directory.mkdirs();
        }
        File csvFile = new File("./csv/" +tableRepresentation.getTableName() + ".csv");
        return new CSVPrinter(new java.io.FileWriter(csvFile), CSVFormat.DEFAULT.withHeader(tableRepresentation.getColumName().toArray(new String[1])));
    }

    @SneakyThrows
    private void printRecord(CSVPrinter printer, String... values) {
        printer.printRecord(values);
    }
}
