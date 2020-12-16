package hr.fer.zemris.java.gui.layouts;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents the position of a cell in a table-like layout.
 *
 * @author Borna Cafuk
 */
public class RCPosition {
    /**
     * The vertical coordinate, 1-indexed.
     * Lower numbers represent rows which are further up.
     */
    public final int row;
    /**
     * The horizontal coordinate, 1-indexed.
     * Lower numbers represent columns which are further left.
     */
    public final int column;

    /**
     * The pattern used by the {@link #parse(String)} method.
     */
    private static final Pattern PARSING_PATTERN = Pattern.compile("([+-]?\\d+),([+-]?\\d+)");

    /**
     * Constructs a coordinate pair.
     *
     * @param row    row index
     * @param column column index
     */
    public RCPosition(int row, int column) {
        this.row = row;
        this.column = column;
    }

    /**
     * Creates an {@code RCPosition} by parsing a string.
     * <p>
     * The string must consist only of two integers separated by a single comma.
     * The individual integers must consist only of decimal digits, optionally prefixed by a {@code -} or {@code +}.
     * <p>
     * The first integer represents the {@link #row}, the second the {@link #column}.
     * <p>
     * Examples of valid strings are "{@code 0,0}", "{@code +1,-2}", "{@code -7,11}", "{@code 162,+59}"
     *
     * @param text the string to be parsed
     * @return the resulting position
     * @throws NullPointerException     if {@code text} is {@code null}
     * @throws IllegalArgumentException if {@code text} does not satisfy the required format
     */
    public static RCPosition parse(String text) {
        Matcher matcher = PARSING_PATTERN.matcher(Objects.requireNonNull(text, "The text must not be null"));

        if (!matcher.matches())
            throw new IllegalArgumentException("Invalid text format: \"" + text + "\"");

        int row = Integer.parseInt(matcher.group(1));
        int column = Integer.parseInt(matcher.group(2));

        return new RCPosition(row, column);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof RCPosition))
            return false;
        RCPosition that = (RCPosition) o;
        return row == that.row && column == that.column;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }

    @Override
    public String toString() {
        return row + "," + column;
    }
}
