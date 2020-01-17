package mt.examples.JDBCtablestocsv.application;

import lombok.SneakyThrows;

import java.sql.Connection;
import java.util.List;

public interface JdbcDbMetadataReader {
    @SneakyThrows
    List<String> getTableNames(Connection connection, String tableNamePattern);
}
