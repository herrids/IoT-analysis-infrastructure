package unipotsdam.myno;

import com.datastax.oss.driver.api.core.CqlSession;

public class CassandraDao {
    private final CqlSession session;

    public CassandraDao(CqlSession session) {
        this.session = session;
    }

    public void saveSensorData(String sensorType, String sensorNumber, String boardUuid, String timestamp, double value) {
        try {
            // if table doesn't exist, create it
            String createTableQuery = String.format("CREATE TABLE IF NOT EXISTS sensor_%s (sensornumber text, board_uuid text, timestamp timestamp, sensorvalue double, PRIMARY KEY (sensornumber, timestamp));", sensorType);
            session.execute(createTableQuery);
    
            // insert data
            String insertDataQuery = String.format("INSERT INTO sensor_%s (sensornumber, board_uuid, timestamp, sensorvalue) VALUES (?, ?, ?, ?);", sensorType);
            session.execute(insertDataQuery, sensorNumber, boardUuid, timestamp, value);
        } catch (com.datastax.oss.driver.api.core.DriverException e) {
            // Handle the exception. Maybe log it, or send a message somewhere notifying you of the issue.
            e.printStackTrace();
        }
    }
    
}
