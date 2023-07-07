package unipotsdam.myno;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class JsonPOJOSerde<T> implements Serializer<T>, Deserializer<T>, Serde<T> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    // Serializer
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public byte[] serialize(String topic, T data) {
        try {
            return objectMapper.writeValueAsBytes(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
    }

    // Deserializer
    @Override
    public T deserialize(String topic, byte[] bytes) {
        try {
            return objectMapper.readValue(bytes, new com.fasterxml.jackson.core.type.TypeReference<T>(){});
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Serde
    @Override
    public Serializer<T> serializer() {
        return this;
    }

    @Override
    public Deserializer<T> deserializer() {
        return this;
    }
}
