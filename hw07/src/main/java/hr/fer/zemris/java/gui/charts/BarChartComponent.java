package hr.fer.zemris.java.gui.charts;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
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
     * How far the axes extend beyond the chart area.
     */
    private static final int ARROW_OVERHANG = 9;
    /**
     * The length of the lines comprising the tips of the arrows.
     */
    private static final int ARROW_LENGTH = 8;
    /**
     * The angle between the axis and the lines comprising the tips of the arrows.
     */
    private static final double ARROW_ANGLE = Math.toRadians(25);

    /**
     * The length of the tick lines.
     */
    private static final int TICK_LENGTH = 4;
    /**
     * The size of the spacing between adjacent axis elements.
     * <p>
     * This determines the spacing between the axis label and the data labels;
     * and between the data labels and the tick marks.
     */
    private static final int AXIS_SPACING = 1;

    /**
     * The amount by which the data columns are shrunk across their whole width.
     */
    private static final int COLUMN_SPACING = 5;

    /**
     * How far one needs to move perpendicular to the direction of the axis to get to the side of the arrow tip.
     */
    private static final int ARROW_OFFSET_LATERAL = (int) Math.round(ARROW_LENGTH * Math.sin(ARROW_ANGLE));
    /**
     * How far one needs to move in the direction of the axis to get to the back of the arrow tip.
     */
    private static final int ARROW_OFFSET_AXIAL =
            ARROW_OVERHANG - (int) Math.round(ARROW_LENGTH * Math.cos(ARROW_ANGLE));

    /**
     * The bar chart model to draw.
     */
    private final BarChart model;

    /**
     * The color to use for the axes, or {@code null} to use the foreground color.
     */
    private Color axisColor = null;
    /**
     * The color to use for the gridlines, or {@code null} to use the foreground color.
     */
    private Color gridlineColor = null;

    /**
     * Constructs a component for the given bar chart.
     *
     * @param model the bar chart to draw
     */
    public BarChartComponent(BarChart model) {
        this.model = Objects.requireNonNull(model, "The model must not be null");
    }

    /**
     * Gets the color used for drawing the axes, or {@code null} if none is set.
     *
     * @return the axis color, or {@code null} if none is set
     */
    public Color getAxisColor() {
        return axisColor;
    }

    /**
     * Sets the color used for drawing the axes.
     *
     * @param axisColor the color to be used for drawing the axes,
     *                  or {@code null} if the foreground color should be used
     */
    public void setAxisColor(Color axisColor) {
        this.axisColor = axisColor;
    }

    /**
     * Gets the color used for drawing the gridlines, or {@code null} if none is set.
     *
     * @return the gridline color, or {@code null} if none is set
     */
    public Color getGridlineColor() {
        return gridlineColor;
    }

    /**
     * Sets the color used for drawing the gridlines.
     *
     * @param gridlineColor the color to be used for drawing the gridlines,
     *                      or {@code null} if the foreground color should be used
     */
    public void setGridlineColor(Color gridlineColor) {
        this.gridlineColor = gridlineColor;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Objects.requireNonNull(g, "The graphics object must not be null");

        if (isOpaque()) {
            g.setColor(getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());
        }

        Frame frame = getChartAreaFrame();

        g.setColor(axisColor == null ? getForeground() : axisColor);
        paintXAxis(g, frame);
        paintYAxis(g, frame);

        g.setColor(gridlineColor == null ? getForeground() : gridlineColor);
        paintGridlines(g, frame);

        g.setColor(getForeground());
        paintChartArea(g, frame);
    }

    @Override
    public Dimension getPreferredSize() {
        return getMinimumSize();
    }

    @Override
    public Dimension getMinimumSize() {
        FontMetrics fm = getFontMetrics(getFont());

        int xDataLabelsWidth = model.getData()
                                    .stream()
                                    .map(xy -> xy.x)
                                    .map(x -> Integer.toString(x))
                                    .map(fm::stringWidth)
                                    .max(Integer::compare)
                                    .orElse(0);
        int columnWidth = Math.max(xDataLabelsWidth, 1 + COLUMN_SPACING);

        int yDataLabelsWidth = fm.stringWidth(Integer.toString(model.getyMax()));
        int yDataLabelCount = (model.getyMax() - model.getyMin()) / model.getyStep();

        int minX = 0;
        // y axis
        minX += 1 + TICK_LENGTH + fm.getHeight() + yDataLabelsWidth + 2 * AXIS_SPACING;
        // chart area
        minX += Math.max(fm.stringWidth(model.getxAxisLabel()), columnWidth * model.getDataCount());
        // right margin
        minX += ARROW_OVERHANG;

        int minY = 0;
        // top margin
        minY += ARROW_OVERHANG;
        // chart area
        minY += Math.max(fm.stringWidth(model.getyAxisLabel()), fm.getHeight() * (yDataLabelCount - 1));
        // x axis
        minY += 1 + TICK_LENGTH + 2 * fm.getHeight() + 2 * AXIS_SPACING;

        return new Dimension(minX, minY);
    }

    /**
     * Calculates the position of the frame of the chart area.
     *
     * @return the frame of pixels just outside the chart area
     */
    private Frame getChartAreaFrame() {
        Insets insets = getInsets();
        FontMetrics fm = getFontMetrics(getFont());

        int yDataLabelsWidth = fm.stringWidth(Integer.toString(model.getyMax()));

        int left = fm.getHeight() + yDataLabelsWidth + TICK_LENGTH + 2 * AXIS_SPACING + insets.left;
        int right = getWidth() - ARROW_OVERHANG - insets.right;
        int top = ARROW_OVERHANG - 1 + insets.top;
        int bottom = getHeight() - fm.getHeight() * 2 - TICK_LENGTH - 2 * AXIS_SPACING - 1 - insets.bottom;

        return new Frame(left, right, top, bottom);
    }

    /**
     * Paints the <i>x</i> axis to {@code g} in the current color.
     *
     * @param g     the {@link Graphics} object to draw to
     * @param frame the frame of pixels just outside the chart area
     */
    private void paintXAxis(Graphics g, Frame frame) {
        FontMetrics fm = getFontMetrics(getFont());

        // Axis line
        g.drawLine(frame.left, frame.bottom, frame.right + ARROW_OVERHANG - 1, frame.bottom);

        // Arrow tip
        g.drawLine(frame.right + ARROW_OVERHANG - 1, frame.bottom,
                frame.right + BarChartComponent.ARROW_OFFSET_AXIAL - 1,
                frame.bottom - BarChartComponent.ARROW_OFFSET_LATERAL);
        g.drawLine(frame.right + ARROW_OVERHANG - 1, frame.bottom,
                frame.right + BarChartComponent.ARROW_OFFSET_AXIAL - 1,
                frame.bottom + BarChartComponent.ARROW_OFFSET_LATERAL);

        // Axis label
        int axisLabelBaseline = getHeight() - getInsets().bottom - fm.getDescent() - 1;
        g.drawString(model.getxAxisLabel(),
                (frame.left + frame.right - fm.stringWidth(model.getxAxisLabel())) / 2, axisLabelBaseline);

        // The left most tick line
        g.drawLine(frame.left, frame.bottom,
                frame.left, frame.bottom + TICK_LENGTH);

        int columnLabelBaseline = axisLabelBaseline - fm.getHeight() - AXIS_SPACING;
        for (int i = 0; i < model.getDataCount(); i++) {
            int barLeft = getAreaX(frame, i);
            int barRight = getAreaX(frame, i + 1) - 1;

            // Tick mark
            g.drawLine(barRight, frame.bottom, barRight, frame.bottom + TICK_LENGTH);

            // Column label
            String columnLabel = Integer.toString(model.getData().get(i).x);
            g.drawString(columnLabel,
                    (barLeft + barRight - fm.stringWidth(columnLabel)) / 2, columnLabelBaseline);
        }
    }

    /**
     * Paints the <i>y</i> axis to {@code g} in the current color.
     *
     * @param g     the {@link Graphics} object to draw to
     * @param frame the frame of pixels just outside the chart area
     */
    private void paintYAxis(Graphics g, Frame frame) {
        FontMetrics fm = getFontMetrics(getFont());

        // Axis line
        g.drawLine(frame.left, frame.bottom, frame.left, frame.top - ARROW_OVERHANG + 1);

        // Arrow tip
        g.drawLine(frame.left, frame.top - ARROW_OVERHANG + 1,
                frame.left - BarChartComponent.ARROW_OFFSET_LATERAL,
                frame.top - BarChartComponent.ARROW_OFFSET_AXIAL + 1);
        g.drawLine(frame.left, frame.top - ARROW_OVERHANG + 1,
                frame.left + BarChartComponent.ARROW_OFFSET_LATERAL,
                frame.top - BarChartComponent.ARROW_OFFSET_AXIAL + 1);

        // Axis label
        int axisLabelBaseline = getInsets().left + fm.getAscent() - 1;
        drawVerticalString(g, model.getyAxisLabel(),
                axisLabelBaseline, (frame.top + frame.bottom + fm.stringWidth(model.getyAxisLabel())) / 2);

        for (int y = model.getyMin(); y <= model.getyMax(); y += model.getyStep()) {
            int lineY = getAreaY(frame, y);

            // Tick mark
            g.drawLine(frame.left, lineY, frame.left - TICK_LENGTH, lineY);

            // Y data label
            String rowLabel = Integer.toString(y);
            int rowLabelWidth = fm.stringWidth(rowLabel);
            g.drawString(rowLabel,
                    frame.left - TICK_LENGTH - AXIS_SPACING - rowLabelWidth, lineY + fm.getAscent() / 2);
        }
    }

    /**
     * Draws a string to the {@code g}, but instead of the text going from left to right, it goes from bottom to top.
     *
     * @param g    the {@link Graphics} object to draw to
     * @param text the text to be written
     * @param x    the <i>x</i> coordinate of the baseline
     * @param y    the <i>y</i> coordinate of the bottom edge of the text
     */
    private void drawVerticalString(Graphics g, String text, int x, int y) {
        Graphics2D g2d = (Graphics2D) g.create();

        AffineTransform af = g2d.getTransform();
        af.quadrantRotate(3);
        g2d.setTransform(af);

        g2d.drawString(text, -y, x);

        g2d.dispose();
    }

    /**
     * Paints the gridlines.
     *
     * @param g     the {@link Graphics} object to draw to
     * @param frame the frame of pixels just outside the chart area
     */
    private void paintGridlines(Graphics g, Frame frame) {
        for (int x = 0; x < model.getDataCount(); x++) {
            int barRight = getAreaX(frame, x + 1) - 1;
            g.drawLine(barRight, frame.top + 1, barRight, frame.bottom - 1);
        }

        for (int y = model.getyMin() + model.getyStep(); y <= model.getyMax(); y += model.getyStep()) {
            int lineY = getAreaY(frame, y);
            g.drawLine(frame.left + 1, lineY, frame.right - 1, lineY);
        }
    }

    /**
     * Paints the chart area (data bars).
     *
     * @param g     the {@link Graphics} object to draw to
     * @param frame the frame of pixels just outside the chart area
     */
    private void paintChartArea(Graphics g, Frame frame) {
        int spacingLeft = COLUMN_SPACING / 2;
        int spacingRight = COLUMN_SPACING - spacingLeft;

        for (int x = 0; x < model.getDataCount(); x++) {
            int barLeft = getAreaX(frame, x) + spacingLeft;
            int barRight = getAreaX(frame, x + 1) - spacingRight;

            int y = model.getData().get(x).y;

            if (y == model.getyMin())
                continue;

            int barTop = getAreaY(frame, y);

            g.fillRect(barLeft, barTop, barRight - barLeft, frame.bottom - barTop);
        }
    }

    /**
     * Transforms an <i>x</i>-coordinate from chart-space to screen-space.
     *
     * @param frame the frame of pixels just outside the chart area
     * @param x     the chart-space <i>x</i> coordinate
     * @return the screen-space <i>x</i> coordinate
     */
    private int getAreaX(Frame frame, int x) {
        double barWidth = (double) (frame.right - frame.left - 1) / model.getDataCount();

        return frame.left + 1 + (int) Math.round(x * barWidth);
    }

    /**
     * Transforms a <i>y</i>-coordinate from chart-space to screen-space.
     *
     * @param frame the frame of pixels just outside the chart area
     * @param y     the chart-space <i>y</i> coordinate
     * @return the screen-space <i>y</i> coordinate
     */
    private int getAreaY(Frame frame, int y) {
        int lineCount = model.getyMax() - model.getyMin();
        double lineHeight = (double) (frame.bottom - frame.top - 1) / lineCount;

        return frame.bottom - (int) Math.round((y - model.getyMin()) * lineHeight);
    }

    /**
     * Used to store information about the size of the chart area.
     */
    private static class Frame {
        public int left;
        public int right;
        public int top;
        public int bottom;

        public Frame(int left, int right, int top, int bottom) {
            this.left = left;
            this.right = right;
            this.top = top;
            this.bottom = bottom;
        }
    }
}
