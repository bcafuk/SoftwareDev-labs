package hr.fer.oprpp1.custom.collections;

/**
 * A generic collection of objects.
 *
 * @param <E> the type of objects to be stored in the collection
 * @author Borna Cafuk
 */
public interface Collection<E> {
    /**
     * Returns whether the collection is empty.
     *
     * @return {@code true} if the collection is empty, {@code false} otherwise
     */
    default boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Returns the size of the collection.
     *
     * @return the number of objects currently stored in the collection
     */
    int size();

    /**
     * Adds an element into the collection. A collection may contain multiple occurrences of an object.
     *
     * @param value the object to be added
     */
    void add(E value);

    /**
     * Tests whether the collection contains an object.
     * <p>
     * Whether an object in the collection is equal to the parameter is determined using the
     * {@link Object#equals(Object)} method.
     *
     * @param value the object to be tested, may be {@code null}
     * @return {@code true} if the collection contains {@code value}, {@code false} otherwise
     */
    boolean contains(Object value);

    /**
     * Removes one occurrence of an object from the collection.
     * <p>
     * Whether an object in the collection is equal to the parameter is determined using the
     * {@link Object#equals(Object)} method.
     *
     * @param value the object to be removed
     * @return {@code true} if an occurrence of {@code value} was found and removed, {@code false} otherwise
     */
    boolean remove(Object value);

    /**
     * Creates an array from the collection.
     *
     * @return an array containing all objects contained in the collection; never {@code null}
     */
    Object[] toArray();

    /**
     * Runs a {@link Processor}'s {@link Processor#process(Object)} method for every object in the collection.
     *
     * @param processor the {@link Processor} to use
     */
    default void forEach(Processor<? super E> processor) {
        createElementsGetter().processRemaining(processor);
    }

    /**
     * Adds all elements from another collection into itself.
     *
     * @param other the collection to add the elements from; remains unchanged
     */
    default void addAll(Collection<? extends E> other) {
        other.forEach(this::add);
    }

    /**
     * Removes all elements from the collection.
     */
    void clear();

    /**
     * Creates an {@link ElementsGetter} for the collection.
     *
     * @return a new ElementsGetter
     */
    ElementsGetter<E> createElementsGetter();

    /**
     * Adds all elements from another collection which pass some test.
     *
     * @param col    the collection whose elements will be added
     * @param tester the tester to filter which elements to add
     */
    default void addAllSatisfying(Collection<? extends E> col, Tester<? super E> tester) {
        col.createElementsGetter().processRemaining(element -> {
            if (tester.test(element))
                add(element);
        });
    }
}
