package hr.fer.oprpp1.custom.collections;

/**
 * An exception that occurs when trying to remove an element from an empty {@link ObjectStack}.
 *
 * @author Borna Cafuk
 */
public class EmptyStackException extends RuntimeException {
    /**
     * Constructs a new empty stack exception.
     */
    public EmptyStackException() {}
}
