package mt.examples.JDBCtablestocsv.application;

import lombok.SneakyThrows;
import mt.examples.JDBCtablestocsv.domain.TableRepresentation;

public interface CsvWriter {
    @SneakyThrows
    void writeToCsv(TableRepresentation tableRepresentation);
}
