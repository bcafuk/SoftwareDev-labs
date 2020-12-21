package hr.fer.zemris.java.gui.calc;

/**
 * Utility functions for use by {@link Calculator} and its components.
 *
 * @author Borna Cafuk
 */
public final class Util {
    /**
     * Converts a {@code double} to a string as with {@link Double#toString()}, but omitting the trailing decimal point
     * and zero ({@code .0}) if the number represents an integer.
     *
     * @param value the value to be formatted
     * @return the formatted string
     */
    public static String formatDouble(double value) {
        if (Math.floor(value) == value)
            return String.format("%.0f", value);
        else
            return Double.toString(value);
    }

    /**
     * Don't let anyone instantiate this class.
     */
    private Util() {}
}
