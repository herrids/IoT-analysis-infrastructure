package unipotsdam.myno;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class StreamProcessor {
    private static final String KAFKA_BROKER = "kafka:9092";
    private static final String INPUT_TOPIC = "sensor-data-topic";
    private static final String CASSANDRA_DB = "cassandra";
    private static final String KEYSPACE = "myno";
    private static final String CASSANDRA_USER = "cassandra";
    private static final String CASSANDRA_PASS = "cassandra";

    private static final Logger logger = LoggerFactory.getLogger(CassandraDao.class);

    private static final Map<String, SensorDataStatistics> statisticsMap = new HashMap<>();

    public static void main(String[] args) {
        // Set up Kafka Streams properties
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "my-stream-processing-application");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_BROKER);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        // Connect to Cassandra database
        CassandraConnector connector = new CassandraConnector();
        connector.connect(CASSANDRA_DB, KEYSPACE, CASSANDRA_USER, CASSANDRA_PASS);
        CassandraDao dao = new CassandraDao(connector.getSession());
        dao.createTableIfNotExists("sensor_statistics", "CREATE TABLE IF NOT EXISTS %s (sensor_type text, sensor_number int, date date, min_value float, max_value float, mean_value float, median_value float, PRIMARY KEY ((sensor_type, sensor_number), date));");

        // Create a Kafka Streams builder
        StreamsBuilder builder = new StreamsBuilder();

        // Create a KStream from the input topic
        KStream<String, String> source = builder.stream(INPUT_TOPIC);

        // Process each record in the stream
        source.foreach((key, value) -> {
            ObjectMapper objectMapper = new ObjectMapper();
            SensorData sensorData;
            try {
                logger.debug(value);
                // Deserialize the JSON value into a SensorData object
                sensorData = objectMapper.readValue(value, SensorData.class);

                // Create a table for the sensor type if it doesn't exist
                dao.createTableIfNotExists("sensor_" + sensorData.getSensorType(), "CREATE TABLE IF NOT EXISTS %s (sensor_number int, board_uuid text, timestamp timestamp, sensor_value double, PRIMARY KEY (sensor_number, timestamp));");

                // Save the sensor data to Cassandra
                dao.saveSensorData(sensorData.getSensorType(), sensorData.getSensorNumber(), sensorData.getBoardUuid(), sensorData.getTimestamp(), sensorData.getValue());

                // Create a key for today's date
                String statsKey = sensorData.getSensorType() + "_" + sensorData.getSensorNumber() + "_" + LocalDate.now();

                // Get the statistics object for the key, or create a new one if it doesn't exist
                SensorDataStatistics stats = statisticsMap.getOrDefault(statsKey, new SensorDataStatistics());

                // Update the statistics with the current sensor data
                stats.updateWith(sensorData);

                // Store the updated statistics object in the map
                statisticsMap.put(statsKey, stats);

                // Create a table for sensor statistics if it doesn't exist
                dao.createTableIfNotExists("sensor_statistics", "CREATE TABLE IF NOT EXISTS %s (sensor_type text, sensor_number int, board_uuid text, date date, min_value float, max_value float, mean_value float, median_value float, PRIMARY KEY ((sensor_type, sensor_number), date));");

                // Save the sensor statistics to Cassandra
                dao.saveSensorStatistics(
                    sensorData.getSensorType(),
                    sensorData.getSensorNumber(),
                    sensorData.getBoardUuid(),
                    LocalDate.now(),
                    (float) stats.getMin(),
                    (float) stats.getMax(),
                    (float) stats.getMean(),
                    (float) stats.getMedian()
                );
            } catch (JsonProcessingException e) {
                logger.error("An error occurred while processing stream", e);
            }
        });

        // Create a Kafka Streams object and start the processing
        KafkaStreams streams = new KafkaStreams(builder.build(), props);
        streams.start();
    }
}
