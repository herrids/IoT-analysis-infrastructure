package unipotsdam.myno;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.Properties;

public class StreamProcessor {
    private static final String KAFKA_BROKER = "kafka:9092";
    private static final String INPUT_TOPIC = "sensor-data-topic";
    private static final String CASSANDRA_DB = "cassandra";
    private static final String KEYSPACE = "myno";
    private static final String CASSANDRA_USER = "cassandra";
    private static final String CASSANDRA_PASS = "cassandra";

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "my-stream-processing-application");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_BROKER);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        CassandraConnector connector = new CassandraConnector();
        connector.connect(CASSANDRA_DB, KEYSPACE, CASSANDRA_USER, CASSANDRA_PASS);
        CassandraDao dao = new CassandraDao(connector.getSession());

        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, String> source = builder.stream(INPUT_TOPIC);

        source.foreach((key, value) -> {
            // Parse your value here, e.g. convert JSON to an object.
            ObjectMapper objectMapper = new ObjectMapper();
            SensorData sensorData;
            try {
                sensorData = objectMapper.readValue(value, SensorData.class);
                // Save to Cassandra.
                dao.saveSensorData(sensorData.getSensorType(), sensorData.getSensorNumber(), sensorData.getBoardUuid(), sensorData.getTimestamp(), sensorData.getValue());
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });

        KafkaStreams streams = new KafkaStreams(builder.build(), props);
        streams.start();
    }
}

