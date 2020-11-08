package hr.fer.oprpp1.hw04.db;

/**
 * Implements comparison operators used in queries.
 *
 * @author Borna Cafuk
 * @see IComparisonOperator
 */
public final class ComparisonOperators {
    /**
     * A character used in the {@code pattern} string in {@link #LIKE}
     * to substitute zero or more characters in the {@code data} string.
     */
    private static final char WILDCARD = '*';

    /**
     * Prevent instantiation.
     */
    private ComparisonOperators() {}

    /**
     * Returns {@code true} if {@code v1} is lexicographically lesser than {@code v2}.
     *
     * @see String#compareTo(String)
     */
    public static final IComparisonOperator LESS = (v1, v2) -> v1.compareTo(v2) < 0;

    /**
     * Returns {@code true} if {@code v1} is lexicographically lesser than or equal to {@code v2}.
     *
     * @see String#compareTo(String)
     */
    public static final IComparisonOperator LESS_OR_EQUALS = (v1, v2) -> v1.compareTo(v2) <= 0;

    /**
     * Returns {@code true} if {@code v1} is lexicographically greater than {@code v2}.
     *
     * @see String#compareTo(String)
     */
    public static final IComparisonOperator GREATER = (v1, v2) -> v1.compareTo(v2) > 0;

    /**
     * Returns {@code true} if {@code v1} is lexicographically lesser than or greater to {@code v2}.
     *
     * @see String#compareTo(String)
     */
    public static final IComparisonOperator GREATER_OR_EQUALS = (v1, v2) -> v1.compareTo(v2) >= 0;

    /**
     * Returns {@code true} if {@code v1} is equal to {@code v2}.
     */
    public static final IComparisonOperator EQUALS = String::equals;

    /**
     * Returns {@code true} if {@code v1} is not equal to {@code v2}.
     */
    public static final IComparisonOperator NOT_EQUALS = (v1, v2) -> !v1.equals(v2);

    /**
     * Checks if {@code data} satisfies a {@code pattern} string.
     * <p>
     * The pattern string may contain at most one {@value WILDCARD} character,
     * which is used as a wildcard that matches zero or more characters in {@code data}.
     * <p>
     * Throws {@link IllegalArgumentException} if {@code pattern} contains more than one {@value WILDCARD} character.
     */
    public static final IComparisonOperator LIKE = (data, pattern) -> {
        int wildcardPosition = pattern.indexOf(WILDCARD);

        if (wildcardPosition == -1)
            return data.equals(pattern);

        if (pattern.indexOf(WILDCARD, wildcardPosition + 1) != -1)
            throw new IllegalArgumentException("Multiple wildcards in pattern " + pattern);

        // The wildcard can't remove characters, so a string like "AAA"
        // can't possibly match a longer pattern like "AA*AA".
        // 1 is subtracted from the pattern length to account for the wildcard.
        if (data.length() < pattern.length() - 1)
            return false;

        String prefix = pattern.substring(0, wildcardPosition);
        String suffix = pattern.substring(wildcardPosition + 1);
        return data.startsWith(prefix) && data.endsWith(suffix);
    };
}
