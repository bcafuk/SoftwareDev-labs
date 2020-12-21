package hr.fer.zemris.java.gui.charts;

/**
 * Represents a data point for a chart.
 *
 * @author Borna Cafuk
 */
public class XYValue {
    /**
     * The <i>x</i> value of the data point.
     */
    public final int x;
    /**
     * The <i>y</i> value of the data point.
     */
    public final int y;

    /**
     * Constructs a new data point.
     *
     * @param x the <i>x</i> value of the data point
     * @param y the <i>y</i> value of the data point
     */
    public XYValue(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
