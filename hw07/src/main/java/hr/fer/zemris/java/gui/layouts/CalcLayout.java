package hr.fer.zemris.java.gui.layouts;

import java.awt.*;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.IntBinaryOperator;

/**
 * A table-like layout for use in a calculator application.
 * <p>
 * The layout contains {@value #ROWS} rows and {@value #COLUMNS} columns.
 * The top left cell is wider and takes up {@value #HOME_WIDTH} columns.
 *
 * @author Borna Cafuk
 */
public class CalcLayout implements LayoutManager2 {
    /**
     * The number of rows in the layout.
     */
    private static final int ROWS = 5;
    /**
     * The number of columns in the layout.
     */
    private static final int COLUMNS = 7;
    /**
     * The width of the home (top-left) cell, in columns.
     */
    private static final int HOME_WIDTH = 5;

    /**
     * The components of the layout.
     * <p>
     * Empty cells contain {@code null}.
     * Cells (1, 2) through (1, {@value HOME_WIDTH}) will always contain {@code null}.
     */
    private final Component[][] components = new Component[ROWS][COLUMNS];
    /**
     * The number of pixels to put between adjacent cells.
     */
    private final int spacing;

    /**
     * Creates a layout with a spacing of 0.
     */
    public CalcLayout() {
        this(0);
    }

    /**
     * Creates a layout with the given number of pixels between adjacent cells.
     *
     * @param spacing the spacing between adjacent cells, in pixels
     * @throws IllegalArgumentException if {@code spacing} is negative
     */
    public CalcLayout(int spacing) {
        if (spacing < 0)
            throw new IllegalArgumentException("The spacing must not be negative, but was " + spacing);

        this.spacing = spacing;
    }

    /**
     * Finds a component in the grid.
     * <p>
     * Components are compared by reference (using {@code =}).
     *
     * @param comp the component to look for
     * @return the position of the component, or {@code null} if it was not found
     * @throws NullPointerException if {@code comp} is {@code null}
     */
    private RCPosition findComponent(Component comp) {
        Objects.requireNonNull(comp, "The component must not be null");

        for (int row = 0; row < ROWS; row++)
            for (int column = 0; column < COLUMNS; column++)
                if (components[row][column] == comp)
                    return new RCPosition(row + 1, column + 1);

        return null;
    }

    /**
     * Adds the specified component to the layout at the specified position.
     * <p>
     * The constraints object may be an {@link RCPosition},
     * or a {@link String} to be parsed using {@link RCPosition#parse(String)}.
     * <p>
     * Positions are 0-indexed, meaning that the allowed rows are [1, {@value #ROWS}],
     * and the allowed columns are [1, {@value #COLUMNS}].
     * The positions "1,2" through "1,{@value HOME_WIDTH}" are invalid
     * because they are covered by the home cell at "1,1".
     * <p>
     * Here is a diagram of valid positions:
     * <pre>
     * [1,1                        ] [1,6] [1,7]
     * [2,1] [2,2] [2,3] [2,4] [2,5] [2,6] [2,7]
     * [3,1] [3,2] [3,3] [3,4] [3,5] [3,6] [3,7]
     * [4,1] [4,2] [4,3] [4,4] [4,5] [4,6] [4,7]
     * [5,1] [5,2] [5,3] [5,4] [5,5] [5,6] [5,7]
     * </pre>
     *
     * @param comp        the component to be added
     * @param constraints where component is added to the layout
     * @throws NullPointerException     if {@code comp} or {@code constraints} is {@code null}
     * @throws IllegalArgumentException if {@code constraints} is neither a {@link RCPosition} nor a {@link String},
     *                                  or if it is a {@link String}, but {@link RCPosition#parse(String)} throws
     * @throws CalcLayoutException      if the position is out of bounds or invalid,
     *                                  if the specified position already contains a component, or
     *                                  if {@code comp} has already been added at a different position
     */
    @Override
    public void addLayoutComponent(Component comp, Object constraints) {
        Objects.requireNonNull(comp, "The component must not be null");
        Objects.requireNonNull(constraints, "The constraints must not be null");

        RCPosition position;
        if (constraints instanceof RCPosition)
            position = (RCPosition) constraints;
        else if (constraints instanceof String)
            position = RCPosition.parse((String) constraints);
        else
            throw new IllegalArgumentException("Invalid constraint type: " + constraints.getClass().getTypeName());

        if (position.row <= 0 || position.row > ROWS)
            throw new CalcLayoutException("Row in " + position + " is out of bounds [1," + ROWS + "]");
        if (position.column <= 0 || position.column > COLUMNS)
            throw new CalcLayoutException("Column in " + position + " is out of bounds [1," + COLUMNS + "]");
        if (position.row == 1 && position.column > 1 && position.column <= HOME_WIDTH)
            throw new CalcLayoutException("The constraint " + position + " overlaps with the home cell");

        if (components[position.row - 1][position.column - 1] != null)
            throw new CalcLayoutException("The cell " + position + " already contains a component");

        RCPosition existingPosition = findComponent(comp);
        if (existingPosition != null)
            throw new CalcLayoutException("The component already exists in the layout at position " + existingPosition);

        components[position.row - 1][position.column - 1] = comp;
    }

    /**
     * Unsupported operation.
     *
     * @param name unused
     * @param comp unused
     * @throws UnsupportedOperationException always
     */
    @Override
    public void addLayoutComponent(String name, Component comp) {
        throw new UnsupportedOperationException("Unsupported operation, use addLayoutComponent(Component, Object) instead");
    }

    /**
     * Removes the specified component from the layout if it exists.
     * <p>
     * A no-op if the component does not exist in the layout.
     *
     * @param comp the component to remove from the layout
     * @throws NullPointerException if {@code comp} is {@code null}
     */
    @Override
    public void removeLayoutComponent(Component comp) {
        RCPosition position = findComponent(Objects.requireNonNull(comp, "The component must not be null"));

        if (position != null)
            components[position.row - 1][position.column - 1] = null;
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        return processLayoutSize(parent, Component::getPreferredSize, Integer::max);
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return processLayoutSize(parent, Component::getMinimumSize, Integer::max);
    }

    @Override
    public Dimension maximumLayoutSize(Container target) {
        return processLayoutSize(target, Component::getMaximumSize, Integer::min);
    }

    /**
     * Calculates a layout size given a getter and an operator to select between values.
     *
     * @param parent   the container to be laid out
     * @param getter   a function used to retrieve the requires {@link Dimension} from a {@link Component}, for example
     *                 {@link Component#getPreferredSize()} or {@link Component#getMinimumSize()}
     * @param operator the operator used to pick between the dimension retrieved from the previous and the next cell's
     *                 dimension, for example {@link Integer#min(int, int)} or {@link Integer#max(int, int)}
     * @return the resulting dimension, or {@code null} if all of the components returned {@code null} in {@code getter}
     * @throws NullPointerException if {@code parent}, {@code getter}, or {@code operator} is {@code null}
     */
    private Dimension processLayoutSize(Container parent,
                                        Function<Component, Dimension> getter, IntBinaryOperator operator) {
        Objects.requireNonNull(parent, "The parent must not be null");
        Objects.requireNonNull(getter, "The getter must not be null");
        Objects.requireNonNull(operator, "The operator must not be null");

        Insets insets = parent.getInsets();

        Dimension componentDimension = getter.apply(components[0][0]);

        if (componentDimension != null) {
            componentDimension = new Dimension(
                    (componentDimension.width - (HOME_WIDTH - 1) * spacing) / HOME_WIDTH,
                    componentDimension.height
            );
        }

        for (int y = 0; y < ROWS; y++) {
            int leftX = (y == 0) ? HOME_WIDTH : 0;
            for (int x = leftX; x < COLUMNS; x++) {
                if (components[y][x] == null)
                    continue;

                Dimension d = getter.apply(components[y][x]);

                if (componentDimension == null) {
                    componentDimension = d;
                } else if (d != null) {
                    componentDimension.width = operator.applyAsInt(componentDimension.width, d.width);
                    componentDimension.height = operator.applyAsInt(componentDimension.height, d.height);
                }
            }
        }

        if (componentDimension == null)
            return null;

        return new Dimension(
                insets.left + insets.right + (COLUMNS - 1) * spacing + COLUMNS * componentDimension.width,
                insets.top + insets.bottom + (ROWS - 1) * spacing + ROWS * componentDimension.height
        );
    }

    /**
     * {@inheritDoc}
     *
     * @param parent the container to be laid out
     * @throws NullPointerException if {@code parent} is {@code null}
     */
    @Override
    public void layoutContainer(Container parent) {
        Objects.requireNonNull(parent, "The parent must not be null");

        Insets insets = parent.getInsets();

        int usableWidth = parent.getWidth() - insets.left - insets.right;
        int usableHeight = parent.getHeight() - insets.top - insets.bottom;

        double componentTotal = ((double) usableWidth - (COLUMNS - 1) * spacing) / COLUMNS;
        double componentHeight = ((double) usableHeight - (ROWS - 1) * spacing) / ROWS;

        for (int y = 0; y < ROWS; y++)
            for (int x = 0; x < COLUMNS; x++)
                if (components[y][x] != null) {
                    double left = insets.left + x * spacing + x * componentTotal;
                    double top = insets.top + y * spacing + y * componentHeight;

                    double right = left + componentTotal;
                    if (x == 0 && y == 0)
                        right += (HOME_WIDTH - 1) * spacing + (HOME_WIDTH - 1) * componentTotal;

                    double bottom = top + componentHeight;

                    components[y][x].setBounds((int) Math.round(left), (int) Math.round(top),
                            (int) Math.round(right) - (int) Math.round(left),
                            (int) Math.round(bottom) - (int) Math.round(top));
                }
    }

    @Override
    public void invalidateLayout(Container target) {}

    @Override
    public float getLayoutAlignmentX(Container target) {
        return 0;
    }

    @Override
    public float getLayoutAlignmentY(Container target) {
        return 0;
    }
}
