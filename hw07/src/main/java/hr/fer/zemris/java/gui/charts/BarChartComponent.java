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

    private static final int ARROW_LENGTH = 8;
    private static final double ARROW_ANGLE = Math.toRadians(25);

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
        int left = 16;
        int right = getWidth() - ARROW_LENGTH - 1;
        int top = ARROW_LENGTH;
        int bottom = getHeight() - 16;

        return new Rectangle(left + 1, top + 1, right - left - 1, bottom - top - 1);
    }

    /**
     * Paints the chart axes.
     *
     * @param g      the {@link Graphics} object to draw to
     * @param bounds the bounds of the chart area
     */
    private void paintAxes(Graphics g, Rectangle bounds) {
        int left = bounds.x - 1;
        int right = bounds.x + bounds.width;
        int top = bounds.y - 1;
        int bottom = bounds.y + bounds.height;

        int arrowOffsetAxial = (int) Math.round(ARROW_LENGTH * Math.cos(ARROW_ANGLE)) - ARROW_LENGTH;
        int arrowOffsetLateral = (int) Math.round(ARROW_LENGTH * Math.sin(ARROW_ANGLE));

        g.setColor(getForeground());

        // x axis
        g.drawLine(left, bottom, right + ARROW_LENGTH, bottom);
        g.drawLine(right + ARROW_LENGTH, bottom, right - arrowOffsetAxial, bottom - arrowOffsetLateral);
        g.drawLine(right + ARROW_LENGTH, bottom, right - arrowOffsetAxial, bottom + arrowOffsetLateral);

        // y axis
        g.drawLine(left, bottom, left, top - ARROW_LENGTH);
        g.drawLine(left, top - ARROW_LENGTH, left - arrowOffsetLateral, top + arrowOffsetAxial);
        g.drawLine(left, top - ARROW_LENGTH, left + arrowOffsetLateral, top + arrowOffsetAxial);
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
