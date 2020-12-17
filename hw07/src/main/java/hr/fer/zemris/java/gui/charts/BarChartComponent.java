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

    private static final int ARROW_OVERHANG = 9;
    private static final int ARROW_LENGTH = 8;
    private static final double ARROW_ANGLE = Math.toRadians(25);
    private static final int TICK_LENGTH = 4;
    private static final int AXIS_SPACING = 1;
    private static final int COLUMN_SPACING = 1;

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
        FontMetrics fm = getFontMetrics(getFont());

        int yDataLabelsWidth = fm.stringWidth(Integer.toString(model.getyMax()));

        int left = fm.getHeight() + yDataLabelsWidth + TICK_LENGTH + 2 * AXIS_SPACING;
        int right = getWidth() - ARROW_OVERHANG;
        int top = ARROW_OVERHANG - 1;
        int bottom = getHeight() - fm.getHeight() * 2 - TICK_LENGTH - 2 * AXIS_SPACING - 1;

        return new Rectangle(left + 1, top + 1, right - left - 1, bottom - top - 1);
    }

    /**
     * Paints the chart axes.
     *
     * @param g      the {@link Graphics} object to draw to
     * @param bounds the bounds of the chart area
     */
    private void paintAxes(Graphics g, Rectangle bounds) {
        FontMetrics fm = getFontMetrics(getFont());

        int left = bounds.x - 1;
        int right = bounds.x + bounds.width;
        int top = bounds.y - 1;
        int bottom = bounds.y + bounds.height;

        int arrowOffsetAxial =ARROW_OVERHANG - (int) Math.round(ARROW_LENGTH * Math.cos(ARROW_ANGLE));
        int arrowOffsetLateral = (int) Math.round(ARROW_LENGTH * Math.sin(ARROW_ANGLE));

        g.setColor(getForeground());

        // x axis
        g.drawLine(left, bottom, right + ARROW_OVERHANG, bottom);
        g.drawLine(right + ARROW_OVERHANG, bottom,right + arrowOffsetAxial, bottom - arrowOffsetLateral);
        g.drawLine(right + ARROW_OVERHANG, bottom,right + arrowOffsetAxial, bottom + arrowOffsetLateral);

        int xAxisLabelBaseline = getHeight() - fm.getDescent() - 1;
        int columnLabelBaseline = xAxisLabelBaseline - fm.getHeight() - AXIS_SPACING;

        g.drawString(model.getxAxisLabel(),
                (left + right - fm.stringWidth(model.getxAxisLabel())) / 2, xAxisLabelBaseline);

        g.drawLine(left, bottom, left, bottom + TICK_LENGTH);

        int barCount = model.getxMax() - model.getxMin() + 1;
        double barWidth = (double) (right - left - 1) / barCount;
        for (int i = 0; i < barCount; i++) {
            int barLeft = left + (int) Math.round(i * barWidth);
            int barRight = left + (int) Math.round((i + 1) * barWidth);

            g.drawLine(barRight, bottom, barRight, bottom + TICK_LENGTH);

            String columnLabel = Integer.toString(i + model.getxMin());
            g.drawString(columnLabel,
                    (barLeft + barRight - fm.stringWidth(columnLabel)) / 2, columnLabelBaseline);
        }

        // y axis
        g.drawLine(left, bottom, left, top - ARROW_OVERHANG);
        g.drawLine(left, top - ARROW_OVERHANG,left - arrowOffsetLateral, top - arrowOffsetAxial);
        g.drawLine(left, top - ARROW_OVERHANG,left + arrowOffsetLateral, top - arrowOffsetAxial);

        int lineCount = (model.getyMax() - model.getyMin()) / model.getyStep();
        double lineHeight = (double) (bottom - top - 1) / lineCount;
        for (int i = 0; i <= lineCount; i++) {
            int lineY = bottom - (int) Math.round(i * lineHeight);

            g.drawLine(left, lineY, left - TICK_LENGTH, lineY);

            String rowLabel = Integer.toString(i * model.getyStep() + model.getyMin());
            int rowLabelWidth = fm.stringWidth(rowLabel);
            g.drawString(rowLabel,
                    left - TICK_LENGTH - AXIS_SPACING - rowLabelWidth, lineY + fm.getAscent() / 2);
        }
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
