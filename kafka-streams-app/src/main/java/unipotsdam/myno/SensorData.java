package unipotsdam.myno;

public class SensorData {
    private String sensorType;
    private String sensorNumber;
    private String boardUuid;
    private String timestamp;
    private double value;

    public String getSensorType() {
        return sensorType;
    }

    public void setSensorType(String sensorType) {
        this.sensorType = sensorType;
    }

    public String getSensorNumber() {
        return sensorNumber;
    }

    public void setSensorNumber(String sensorNumber) {
        this.sensorNumber = sensorNumber;
    }

    public String getBoardUuid() {
        return boardUuid;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public double getValue() {
        return value;
    }

}

