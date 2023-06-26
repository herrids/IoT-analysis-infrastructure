package unipotsdam.myno;

import com.datastax.oss.driver.api.core.CqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CassandraDao {
    private final CqlSession session;
    private static final Logger logger = LoggerFactory.getLogger(CassandraDao.class);

    public CassandraDao(CqlSession session) {
        this.session = session;
    }

    public void saveSensorData(String sensorType, String sensorNumber, String boardUuid, Long timestamp, double value) {
        try {
            // if table doesn't exist, create it
            String createTableQuery = String.format("CREATE TABLE IF NOT EXISTS sensor_%s (sensornumber text, board_uuid text, timestamp timestamp, sensorvalue double, PRIMARY KEY (sensornumber, timestamp));", sensorType);
            session.execute(createTableQuery);
    
            // insert data
            String insertDataQuery = String.format("INSERT INTO sensor_%s (sensornumber, board_uuid, timestamp, sensorvalue) VALUES (?, ?, ?, ?);", sensorType);
            session.execute(insertDataQuery, sensorNumber, boardUuid, timestamp, value);
        } catch (com.datastax.oss.driver.api.core.DriverException e) {
            logger.debug(sensorNumber);
            logger.debug(boardUuid);
            logger.debug(Long.toString(timestamp));
            logger.debug(Double.toString(value));
            logger.error("An error occurred while saving sensor data", e);
        }
    }
    
}
