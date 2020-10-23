package hr.fer.oprpp1.custom.collections;

/**
 * Implementations of this interface are used to get the elements of a {@link Collection}.
 */
public interface ElementsGetter {
    /**
     * Returns whether if the collection has more elements.
     *
     * @return {@code true} if the collection has more elements, {@code false} otherwise
     */
    boolean hasNextElement();

    /**
     * Gets the next element in the collection
     *
     * @return the next element in the collection
     * @throws java.util.NoSuchElementException if there are no more elements
     */
    Object getNextElement();
}
