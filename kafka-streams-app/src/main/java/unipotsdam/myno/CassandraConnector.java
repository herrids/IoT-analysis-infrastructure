package unipotsdam.myno;

import com.datastax.oss.driver.api.core.CqlSession;
import java.net.InetSocketAddress;

public class CassandraConnector {
    private CqlSession session;

    public void connect(String node, String keyspace, String username, String password) {
        session = CqlSession.builder()
                    .addContactPoint(new InetSocketAddress(node, 9042))
                    .withKeyspace(keyspace)
                    .withAuthCredentials(username, password)
                    .withLocalDatacenter("datacenter1")
                    .build();
                    
        initializeSchema();
    }
    

    public CqlSession getSession() {
        return this.session;
    }

    public void close() {
        session.close();
    }

    public void initializeSchema() {
        session.execute("CREATE KEYSPACE IF NOT EXISTS myno WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 }");
        session.execute("USE myno");
        session.execute("CREATE TABLE IF NOT EXISTS sensor_data (sensor_id text, value double, PRIMARY KEY (sensor_id))");
    }
    
}
