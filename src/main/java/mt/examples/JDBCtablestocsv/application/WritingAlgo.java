package mt.examples.JDBCtablestocsv.application;

import org.hibernate.internal.SessionImpl;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Connection;
import java.util.List;

@Component
public class WritingAlgo {
    @PersistenceContext
    EntityManager em;

    @Transactional
    public void run(JdbcDbMetadataReader jdbcDbMetadataReader, TableReader tableReader, FileWriter fileWriter, String tablePattern){
        Connection connection = em.unwrap(SessionImpl.class).connection();
        List<String> tableNames = jdbcDbMetadataReader.getTableNames(connection, tablePattern);

        tableNames.stream()
                .peek(tableName -> System.out.println("Reading table name " + tableName))
                .map(tableName -> tableReader.readTable(connection, tableName))
                .forEach(fileWriter::writeToFile);
    }
}
