package hr.fer.oprpp1.custom.collections;

/**
 * Represents an operation that accepts a single input argument and returns no result.
 *
 * @param <T> the type of the input to the operation
 * @author Borna Cafuk
 * @see java.util.function.Consumer
 */
@FunctionalInterface
public interface Processor<T> {
    /**
     * Performs this operation on the given argument.
     *
     * @param t the input argument
     */
    void process(T t);
}
