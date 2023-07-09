package unipotsdam.myno;

import java.util.*;

public class SensorDataStatistics {
    private List<Double> values = new ArrayList<>();
    private double min;
    private double max;
    private double mean;
    private double median;

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

    // Getters and setters for min, max, mean, median
    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public double getMean() {
        return mean;
    }

    public void setMean(double mean) {
        this.mean = mean;
    }

    public double getMedian() {
        return median;
    }

    public void setMedian(double median) {
        this.median = median;
    }

    // Update statistics with new data
    public SensorDataStatistics updateWith(SensorData data) {
        values.add(data.getValue());
        Collections.sort(values);
        min = getMinValue();
        max = getMaxValue();
        mean = getMeanValue();
        median = getMedianValue();
        return this;
    }

    private double getMinValue() {
        if (values.isEmpty()) {
            return Double.NaN;
        }
        return values.get(0);
    }

    private double getMaxValue() {
        if (values.isEmpty()) {
            return Double.NaN;
        }
        return values.get(values.size() - 1);
    }

    private double getMeanValue() {
        if (values.isEmpty()) {
            return Double.NaN;
        }
        double sum = 0.0;
        for (double value : values) {
            sum += value;
        }
        return sum / values.size();
    }

    private double getMedianValue() {
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
