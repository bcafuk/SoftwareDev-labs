package hr.fer.zemris.java.gui.charts;

import java.util.*;

/**
 * The model of a read-only bar chart.
 *
 * @author Borna Cafuk
 */
public class BarChart {
    /**
     * An unmodifiable list od data point <i>y</i> values, with {@code null} signifying no data.
     */
    private final List<Integer> data;
    /**
     * The <i>x</i> value of the first data point.
     */
    private final int xMin;
    /**
     * The <i>x</i> value of the last data point.
     */
    private final int xMax;
    /**
     * The <i>y</i> value of the bottom of the chart.
     */
    private final int yMin;
    /**
     * The <i>y</i> value of the top of the chart.
     */
    private final int yMax;
    /**
     * The distance between two consecutive labels on the <i>y</i> axis.
     */
    private final int yStep;
    /**
     * The label of the <i>x</i> axis.
     */
    private final String xAxisLabel;
    /**
     * The label of the <i>y</i> axis.
     */
    private final String yAxisLabel;

    /**
     * Creates a new read-only bar chart.
     *
     * @param dataPoints a list of data points
     * @param xAxisLabel the label of the <i>x</i> axis
     * @param yAxisLabel the label of the <i>y</i> axis
     * @param yMin       the <i>y</i> value at the bottom of the chart
     * @param yMax       the <i>y</i> value at the top of the chart
     * @param yStep      the distance between two consecutive labels on the <i>y</i> axis
     * @throws NullPointerException     if {@code dataPoints}, any of the data points,
     *                                  {@code xAxisLabel} or {@code yAxisLabel} is {@code null}
     * @throws IllegalArgumentException if <ul>
     *                                  <li>{@code yMin} < 0,</li>
     *                                  <li>{@code yMax} &le; {@code yMin},</li>
     *                                  <li>{@code yMin} &le; 0,</li>
     *                                  <li>{@code dataPoints} is empty,</li>
     *                                  <li>any of the data points' <i>y</i> value is less than {@code yMin}, or</li>
     *                                  <li>if {@code dataPoints} contains multiple points
     *                                  with the same <i>x</i> value</li>
     *                                  </ul>
     */
    public BarChart(List<XYValue> dataPoints, String xAxisLabel, String yAxisLabel, int yMin, int yMax, int yStep) {
        if (yMin < 0)
            throw new IllegalArgumentException("The minimum y value must not be negative, but was " + yMin);
        if (yMax <= yMin)
            throw new IllegalArgumentException("The maximum y value must be greater than the minimum value (" + yMin +
                    "), but was " + yMax);
        if (yStep <= 0)
            throw new IllegalArgumentException("The y step must be positive, but was " + yStep);

        Objects.requireNonNull(xAxisLabel, "The x axis label must not be null");
        Objects.requireNonNull(yAxisLabel, "The y axis label must not be null");
        Objects.requireNonNull(dataPoints, "The data point list must not be null");

        if (dataPoints.isEmpty())
            throw new IllegalArgumentException("The data point list must contain at least one data point");

        int xMin = Integer.MAX_VALUE;
        int xMax = Integer.MIN_VALUE;

        TreeMap<Integer, Integer> dataMap = new TreeMap<>();
        for (XYValue dataPoint : dataPoints) {
            Objects.requireNonNull(dataPoint, "All of the data points must not be null");

            if (dataPoint.y < yMin)
                throw new IllegalArgumentException("Data point @x=" + dataPoint.x + " has a y=" + dataPoint.y +
                        " smaller than the minimum (" + yMin + ")");

            if (dataMap.putIfAbsent(dataPoint.x, dataPoint.y) != null)
                throw new IllegalArgumentException("Duplicate data point for x=" + dataPoint.x);

            xMin = Math.min(xMin, dataPoint.x);
            xMax = Math.max(xMax, dataPoint.x);
        }

        List<Integer> data = new ArrayList<>(xMax - xMin + 1);
        for (int x = xMin; x <= xMax; x++)
            data.add(dataMap.get(x));
        this.data = Collections.unmodifiableList(data);

        this.xMin = xMin;
        this.xMax = xMax;

        int yMaxRemainder = (yMax - yMin) % yStep;
        if (yMaxRemainder != 0)
            yMax += yStep - yMaxRemainder;

        this.yMin = yMin;
        this.yMax = yMax;
        this.yStep = yStep;

        this.xAxisLabel = xAxisLabel;
        this.yAxisLabel = yAxisLabel;
    }

    /**
     * Returns an unmodifiable list of data point <i>y</i> values, with {@code null} signifying no data.
     * <p>
     * The first element has the <i>x</i> value returned by {@link #getxMin()},
     * and the last has the value returned by {@link #getxMax()}.
     *
     * @return an unmodifiable list of data points
     */
    public List<Integer> getData() {
        return data;
    }

    /**
     * The <i>x</i> value of the leftmost data point.
     *
     * @return the <i>x</i> value of the leftmost data point
     */
    public int getxMin() {
        return xMin;
    }

    /**
     * The <i>x</i> value of the rightmost data point.
     *
     * @return the <i>x</i> value of the rightmost data point
     */
    public int getxMax() {
        return xMax;
    }

    /**
     * The <i>y</i> value of the bottom of the chart.
     *
     * @return the <i>y</i> value of the bottom of the chart
     */
    public int getyMin() {
        return yMin;
    }

    /**
     * The <i>y</i> value of the top of the chart.
     *
     * @return the <i>y</i> value of the top of the chart
     */
    public int getyMax() {
        return yMax;
    }

    /**
     * The distance between two consecutive labels on the <i>y</i> axis.
     *
     * @return the distance between two consecutive labels on the <i>y</i> axis
     */
    public int getyStep() {
        return yStep;
    }

    /**
     * The label of the <i>x</i> axis.
     *
     * @return the label of the <i>x</i> axis
     */
    public String getxAxisLabel() {
        return xAxisLabel;
    }

    /**
     * The label of the <i>y</i> axis.
     *
     * @return the label of the <i>y</i> axis
     */
    public String getyAxisLabel() {
        return yAxisLabel;
    }
}