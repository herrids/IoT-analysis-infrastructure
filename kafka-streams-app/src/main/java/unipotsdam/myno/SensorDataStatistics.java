package unipotsdam.myno;

import java.util.*;

public class SensorDataStatistics {
    private List<Double> values = new ArrayList<>();

    public SensorDataStatistics() {
    }

    // Getter for "values"
    public List<Double> getValues() {
        return this.values;
    }

    // Setter for "values"
    public void setValues(List<Double> values) {
        this.values = values;
    }

    // Update statistics with new data
    public SensorDataStatistics updateWith(SensorData data) {
        values.add(data.getValue());
        Collections.sort(values);
        return this;
    }

    public double getMin() {
        if (values.isEmpty()) {
            return Double.NaN;
        }
        return values.get(0);
    }

    public double getMax() {
        if (values.isEmpty()) {
            return Double.NaN;
        }
        return values.get(values.size() - 1);
    }

    public double getMean() {
        if (values.isEmpty()) {
            return Double.NaN;
        }
        double sum = 0.0;
        for (double value : values) {
            sum += value;
        }
        return sum / values.size();
    }

    public double getMedian() {
        if (values.isEmpty()) {
            return Double.NaN;
        }
        int middle = values.size() / 2;
        if (values.size() % 2 == 1) {
            return values.get(middle);
        } else {
            return (values.get(middle - 1) + values.get(middle)) / 2.0;
        }
    }
}
