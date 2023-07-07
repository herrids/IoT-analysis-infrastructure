package unipotsdam.myno;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class SensorDataSerde implements Serde<SensorData> {

    final private Serde<SensorData> inner;
 
    public SensorDataSerde() {
        inner = Serdes.serdeFrom(new SensorDataSerializer(), new SensorDataDeserializer());
    }
 
    @Override
    public Serializer<SensorData> serializer() {
        return inner.serializer();
    }
 
    @Override
    public Deserializer<SensorData> deserializer() {
        return inner.deserializer();
    }
 
    @Override
    public void configure(Map<String, ?> map, boolean b) {
    }
 
    @Override
    public void close() {
    }

    private static class SensorDataSerializer implements Serializer<SensorData> {
        private final ObjectMapper objectMapper = new ObjectMapper();

        @Override
        public byte[] serialize(String topic, SensorData data) {
            try {
                return objectMapper.writeValueAsBytes(data);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static class SensorDataDeserializer implements Deserializer<SensorData> {
        private final ObjectMapper objectMapper = new ObjectMapper();

        @Override
        public SensorData deserialize(String topic, byte[] data) {
            try {
                return objectMapper.readValue(data, SensorData.class);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}

