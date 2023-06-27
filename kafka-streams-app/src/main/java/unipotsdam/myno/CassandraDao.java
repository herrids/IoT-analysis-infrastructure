package unipotsdam.myno;

import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDate;

public class CassandraDao {
    private final CqlSession session;
    private static final Logger logger = LoggerFactory.getLogger(CassandraDao.class);

    public CassandraDao(CqlSession session) {
        this.session = session;
    }

    public void createTableIfNotExists(String tableName, String schema) {
        String createTableQuery = String.format(schema, tableName);
        session.execute(createTableQuery);
    }

    public void saveSensorData(String sensorType, int sensorNumber, String boardUuid, Long timestamp, double value) {
        logger.debug(sensorType + ", " + sensorNumber + ", " + boardUuid + ", " + timestamp + ", " + value);
        try {
            String insertDataQuery = String.format("INSERT INTO sensor_%s (sensor_number, board_uuid, timestamp, sensor_value) VALUES (?, ?, ?, ?);", sensorType);
            PreparedStatement preparedStatement = session.prepare(insertDataQuery);

            BoundStatement boundStatement = preparedStatement.bind(sensorNumber, boardUuid, timestamp, value);
            session.execute(boundStatement);
        } catch (com.datastax.oss.driver.api.core.DriverException e) {
            logger.error("An error occurred while saving sensor data", e);
        }
    }

    public void saveSensorStatistics(String sensorType, int sensorNumber, LocalDate date, float minValue, float maxValue, float meanValue, float medianValue) {
        logger.debug(sensorType + ", " + sensorNumber + ", " + date);
        try {
            String insertDataQuery = "INSERT INTO sensor_statistics (sensor_type, sensor_number, date, min_value, max_value, mean_value, median_value) VALUES (?, ?, ?, ?, ?, ?, ?);";
            PreparedStatement preparedStatement = session.prepare(insertDataQuery);

            BoundStatement boundStatement = preparedStatement.bind(sensorType, sensorNumber, date, minValue, maxValue, meanValue, medianValue);
            session.execute(boundStatement);
        } catch (com.datastax.oss.driver.api.core.DriverException e) {
            logger.error("An error occurred while saving sensor data", e);
        }
    }
}
