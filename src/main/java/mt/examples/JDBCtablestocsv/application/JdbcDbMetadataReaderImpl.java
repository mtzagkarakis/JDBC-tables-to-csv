package mt.examples.JDBCtablestocsv.application;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcDbMetadataReaderImpl implements JdbcDbMetadataReader {
    @SneakyThrows
    @Override
    public List<String> getTableNames(Connection connection, String tableNamePattern) {
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
}
