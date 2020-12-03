package hr.fer.oprpp1.hw05.shell;

/**
 * A utility class to be used by the shell and its commands.
 *
 * @author Borna Cafuk
 */
public final class Util {
    /**
     * Don't let anyone instantiate this class.
     */
    private Util() {}

    /**
     * Removes all whitespace from the beginning and end of the string.
     * <p>
     * If {@code s} is {@code null}, {@code null} is returned. If {@code s} is empty, or if it neither starts nor ends
     * with whitespace, a reference to {@code s} is returned. Otherwise, a new string is returned.
     * <p>
     * Unlike {@link String#trim()}, this method trims all whitespace, not just characters less than ({@code 'U+0020'}).
     *
     * @param s the string to trim
     * @return the trimmed whitespace.
     */
    public static String trimWhitespace(String s) {
        if (s == null || s.isEmpty())
            return s;

        if (!Character.isWhitespace(s.charAt(0)) && !Character.isWhitespace(s.charAt(s.length() - 1)))
            return s;

        int endIndex = s.length();
        while (endIndex > 0 && Character.isWhitespace(s.charAt(endIndex - 1)))
            endIndex--;

        int beginIndex = 0;
        while (beginIndex < endIndex && Character.isWhitespace(s.charAt(beginIndex)))
            beginIndex++;

        return s.substring(beginIndex, endIndex);
    }
}
