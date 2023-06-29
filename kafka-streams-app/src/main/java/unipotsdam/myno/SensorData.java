package unipotsdam.myno;

import java.time.Instant;

public class SensorData {
    private String sensorType;
    private Integer sensorNumber;
    private String boardUuid;
    private Long timestamp;
    private double value;

    public String getSensorType() {
        return sensorType;
    }

    public Integer getSensorNumber() {
        return sensorNumber;
    }

    public String getBoardUuid() {
        return boardUuid;
    }

    public Instant getTimestamp() {
        return Instant.ofEpochMilli(timestamp);
    }

    public double getValue() {
        return value;
    }

}

