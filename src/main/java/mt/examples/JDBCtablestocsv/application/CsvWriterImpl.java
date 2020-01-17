package mt.examples.JDBCtablestocsv.application;

import lombok.SneakyThrows;
import mt.examples.JDBCtablestocsv.domain.TableRepresentation;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;

@Component
public class CsvWriterImpl implements CsvWriter {
    @Override
    @SneakyThrows
    public void writeToCsv(TableRepresentation tableRepresentation) {
        try (CSVPrinter printer = createCsvPrinter(tableRepresentation)) {
            tableRepresentation.getRows().stream()
                    .forEach(row -> printRecord(printer, row.toArray(new String[1])));
        }
    }

    @SneakyThrows
    private CSVPrinter createCsvPrinter(TableRepresentation tableRepresentation) {
        File directory = new File("./csv");
        if (!directory.exists()){
            directory.mkdirs();
        }
        File csvFile = new File("./csv/" +tableRepresentation.getTableName() + ".csv");
        return new CSVPrinter(new FileWriter(csvFile), CSVFormat.DEFAULT.withHeader(tableRepresentation.getColumName().toArray(new String[1])));
    }

    @SneakyThrows
    private void printRecord(CSVPrinter printer, String... values) {
        printer.printRecord(values);
    }
}
