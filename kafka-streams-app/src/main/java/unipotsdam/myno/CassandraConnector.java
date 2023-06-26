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
                    .build();
    }
    

    public CqlSession getSession() {
        return this.session;
    }

    public void close() {
        session.close();
    }
}
