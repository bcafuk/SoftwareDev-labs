package hr.fer.oprpp1.custom.collections;

/**
 * Represents a predicate (boolean-valued function) of one argument.
 *
 * @param <T> the type of the input to the tester
 * @author Borna Cafuk
 * @see java.util.function.Predicate
 */
@FunctionalInterface
public interface Tester<T> {
    /**
     * Evaluates this tester on the given argument.
     *
     * @param t the input argument
     * @return {@code true} if the test passes, {@code false} otherwise
     */
    boolean test(T t);
}
