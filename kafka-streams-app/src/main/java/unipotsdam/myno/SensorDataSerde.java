package unipotsdam.myno;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class SensorDataSerde implements Serializer<SensorData>, Deserializer<SensorData>, Serde<SensorData> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // Nothing to configure
    }

    @Override
    public SensorData deserialize(String topic, byte[] data) {
        if (data == null) {
            return null;
        }

        SensorData obj;
        try {
            obj = objectMapper.readValue(data, SensorData.class);
        } catch (Exception e) {
            throw new SerializationException(e);
        }

        return obj;
    }

    @Override
    public byte[] serialize(String topic, SensorData data) {
        if (data == null) {
            return null;
        }

        byte[] retVal;
        try {
            retVal = objectMapper.writeValueAsBytes(data);
        } catch (Exception e) {
            throw new SerializationException(e);
        }

        return retVal;
    }

    @Override
    public void close() {
        // Nothing to close
    }

    @Override
    public Serializer<SensorData> serializer() {
        return this;
    }

    @Override
    public Deserializer<SensorData> deserializer() {
        return this;
    }
}
