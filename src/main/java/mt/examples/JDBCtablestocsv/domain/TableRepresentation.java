package mt.examples.JDBCtablestocsv.domain;

import lombok.Getter;

import java.util.List;
import java.util.Objects;

@Getter
public class TableRepresentation {
    private final String tableName;
    private final List<String> columName;
    private final List<List<String>> rows;

    public TableRepresentation(String tableName, List<String> columName, List<List<String>> rows) {
        Objects.requireNonNull(tableName);
        Objects.requireNonNull(columName);
        Objects.requireNonNull(rows);
        rows.forEach(Objects::requireNonNull);
        this.tableName = tableName;
        this.columName = columName;
        this.rows = rows;
    }
}
