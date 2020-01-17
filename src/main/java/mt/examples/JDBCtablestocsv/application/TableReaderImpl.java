package mt.examples.JDBCtablestocsv.application;

import lombok.SneakyThrows;
import mt.examples.JDBCtablestocsv.domain.TableRepresentation;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class TableReaderImpl implements TableReader{
    @Override
    @SneakyThrows
    public TableRepresentation readTable(Connection connection, String tableName) {
        PreparedStatement ps = connection.prepareStatement("select * from " + tableName);
        return new TableRepresentation(tableName, getColumnNames(ps), getRows(ps));
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
}
