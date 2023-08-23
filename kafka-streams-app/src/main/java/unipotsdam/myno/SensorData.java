package unipotsdam.myno;

import java.time.Instant;

public class SensorData {
    private String sensorType;
    private Integer sensorNumber;
    private String boardUuid;
    private Long timestamp;
    private String value;

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
    String normalizedValue = value.toLowerCase();
    if (normalizedValue.contains("on")) {
        return 1.0;
    } else if (normalizedValue.contains("off")) {
        return 0.0;
    } else {
        try {
            return Double.parseDouble(normalizedValue);
        } catch (NumberFormatException e) {
            return Double.NaN;
        }
    }
}


}

