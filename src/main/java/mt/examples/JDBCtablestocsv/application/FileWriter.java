package mt.examples.JDBCtablestocsv.application;

import lombok.SneakyThrows;
import mt.examples.JDBCtablestocsv.domain.TableRepresentation;

public interface FileWriter {
    @SneakyThrows
    void writeToFile(TableRepresentation tableRepresentation);
}
