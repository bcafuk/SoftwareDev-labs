package hr.fer.oprpp1.hw04.db;

/**
 * A functional interface used to compare strings.
 *
 * @author Borna Cafuk
 * @see java.util.function.BiPredicate
 */
@FunctionalInterface
public interface IComparisonOperator {
    /**
     * Checks if a pair of strings satisfies the operator.
     *
     * @param value1 the first string to compare
     * @param value2 the second string to compare
     * @return {@code true} if the pair of strings satisfies the operator, {@code false} if it does not
     */
    boolean satisfied(String value1, String value2);
}
