package hr.fer.oprpp1.custom.collections;

import java.util.ConcurrentModificationException;

/**
 * Implementations of this interface are used to iterate through the elements of a {@link Collection}.
 *
 * @param <E> the type of the elements
 * @author Borna Cafuk
 */
public interface ElementsGetter<E> {
    /**
     * Returns whether if the collection has more elements.
     *
     * @return {@code true} if the collection has more elements, {@code false} otherwise
     * @throws ConcurrentModificationException if the collection has changed since this {@link ElementsGetter}
     *                                         has been created
     */
    boolean hasNextElement();

    /**
     * Gets the next element in the collection
     *
     * @return the next element in the collection
     * @throws java.util.NoSuchElementException if there are no more elements
     * @throws ConcurrentModificationException  if the collection has changed since this {@link ElementsGetter}
     *                                          has been created
     */
    E getNextElement();

    /**
     * Calls {@link Processor#process(Object)} on all remaining elements.
     *
     * @param p the processor to use
     */
    default void processRemaining(Processor<? super E> p) {
        while (hasNextElement())
            p.process(getNextElement());
    }
}
