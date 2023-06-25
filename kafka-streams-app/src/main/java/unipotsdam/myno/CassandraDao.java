package unipotsdam.myno;

import com.datastax.oss.driver.api.core.CqlSession;

public class CassandraDao {
    private final CqlSession session;

    public CassandraDao(CqlSession session) {
        this.session = session;
    }

    public void saveSensorData(String sensorId, double value) {
        session.execute("INSERT INTO sensor_data (sensor_id, value) VALUES (?, ?)", sensorId, value);
    }

}
