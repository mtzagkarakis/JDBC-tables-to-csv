package mt.examples.JDBCtablestocsv.application;

import lombok.SneakyThrows;
import mt.examples.JDBCtablestocsv.domain.TableRepresentation;

import java.sql.Connection;

public interface TableReader {
    @SneakyThrows
    TableRepresentation readTable(Connection connection, String tableName);
}
