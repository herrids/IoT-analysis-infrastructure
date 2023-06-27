package unipotsdam.myno;

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

    public Long getTimestamp() {
        return timestamp;
    }

    public double getValue() {
        return value;
    }

}

