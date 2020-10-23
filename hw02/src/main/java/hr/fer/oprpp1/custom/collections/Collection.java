package hr.fer.oprpp1.custom.collections;

/**
 * A generic collection of objects.
 *
 * @author Borna Cafuk
 */
public interface Collection {
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
     * Adds an object into the collection. A collection may contain multiple occurrences of an object.
     *
     * @param value the object to be added
     */
    void add(Object value);

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
    void forEach(Processor processor);

    /**
     * Adds all elements from another collection into itself.
     *
     * @param other the collection to add the elements from; remains unchanged
     */
    default void addAll(Collection other) {
        class AddProcessor implements Processor {
            public void process(Object value) {
                add(value);
            }
        }

        other.forEach(new AddProcessor());
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
    ElementsGetter createElementsGetter();
}
