package hr.fer.zemris.java.gui.charts;

import javax.swing.*;
import java.awt.*;
import java.io.Serial;
import java.util.Objects;

/**
 * A component used to display a {@link BarChart}.
 *
 * @author Borna Cafuk
 */
public class BarChartComponent extends JComponent {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * The bar chart model to draw.
     */
    private final BarChart model;

    /**
     * Constructs a component for the given bar chart.
     *
     * @param model the bar chart to draw
     */
    public BarChartComponent(BarChart model) {
        this.model = Objects.requireNonNull(model, "The model must not be null");
    }

    @Override
    protected void paintComponent(Graphics g) {
        Objects.requireNonNull(g, "The graphics object must not be null");

        if (isOpaque()) {
            g.setColor(getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());
        }

        Rectangle bounds = getAreaBounds();
        paintAxes(g, bounds);
        paintChartArea(g, bounds);
    }

    /**
     * Calculates the bounds of the chart area.
     *
     * @return the bounds of the chart area
     */
    private Rectangle getAreaBounds() {
        // TODO: Implement proper calculation
        return new Rectangle(32, 32, getWidth() - 64, getHeight() - 64);
    }

    /**
     * Paints the chart axes.
     *
     * @param g      the {@link Graphics} object to draw to
     * @param bounds the bounds of the chart area
     */
    private void paintAxes(Graphics g, Rectangle bounds) {
        // TODO: Implement drawing
    }

    /**
     * Paints the chart area (data bars).
     *
     * @param g      the {@link Graphics} object to draw to
     * @param bounds the bounds of the chart area
     */
    private void paintChartArea(Graphics g, Rectangle bounds) {
        // TODO: Implement drawing
    }
}
