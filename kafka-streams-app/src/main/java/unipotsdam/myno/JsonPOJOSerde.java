package unipotsdam.myno;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class JsonPOJOSerde<T> implements Serializer<T>, Deserializer<T>, Serde<T> {

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private final Class<T> tClass;

    public JsonPOJOSerde(Class<T> tClass) {
        this.tClass = tClass;
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {}

    @Override
    public T deserialize(String topic, byte[] data) {
        if (data == null) {
            return null;
        }

        T obj;
        try {
            obj = objectMapper.readValue(data, tClass);
        } catch (Exception e) {
            throw new SerializationException(e);
        }

        return obj;
    }

    @Override
    public byte[] serialize(String topic, T data) {
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
    public void close() {}

    @Override
    public Serializer<T> serializer() {
        return this;
    }

    @Override
    public Deserializer<T> deserializer() {
        return this;
    }
}
