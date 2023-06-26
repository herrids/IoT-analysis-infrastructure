package unipotsdam.myno;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;

public class CassandraDao {
    private final CqlSession session;

    public CassandraDao(CqlSession session) {
        this.session = session;
    }

    public void saveSensorData(String sensorType, String sensorNumber, String boardUuid, String timestamp, double value) {
        // check if table exists
        String checkTableQuery = String.format("SELECT table_name FROM system_schema.tables WHERE keyspace_name = 'myno' AND table_name = 'sensor_%s';", sensorType);
        ResultSet resultSet = session.execute(checkTableQuery);
        Row row = resultSet.one();
        
        // if table doesn't exist, create it
        if (row == null) {
            String createTableQuery = String.format("CREATE TABLE sensor_%s (sensornumber text, board_uuid text, timestamp timestamp, sensorvalue double, PRIMARY KEY (sensornumber, timestamp));", sensorType);
            session.execute(createTableQuery);
        }

        // insert data
        String insertDataQuery = String.format("INSERT INTO sensor_%s (sensornumber, board_uuid, timestamp, sensorvalue) VALUES (?, ?, ?, ?);", sensorType);
        session.execute(insertDataQuery, sensorNumber, boardUuid, timestamp, value);
    }
}
