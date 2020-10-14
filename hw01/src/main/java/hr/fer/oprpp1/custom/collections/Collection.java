package hr.fer.oprpp1.custom.collections;

/**
 * A generic collection of objects.
 *
 * @author Borna Cafuk
 */
public class Collection {
    /**
     * This constructor ensures that {@link Collection}s cannot be instantiated except by subclasses.
     *
     * At this point in the lesson plan, abstract classes haven't yet been covered, so I used protected constructors and
     * dummy implementations of methods that are expected to be overridden, as instructed in the assignment document.
     */
    protected Collection() {}

    /**
     * Returns whether the collection is empty.
     *
     * @return {@code true} if the collection is empty, {@code false} otherwise
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Returns the size of the collection.
     *
     * @return the number of objects currently stored in the collection
     */
    public int size() {
        return 0;
    }

    /**
     * Adds an object into the collection. A collection may contain multiple occurrences of an object.
     *
     * @param value the object to be added
     */
    public void add(Object value) {}

    /**
     * Tests whether the collection contains an object.
     *
     * Whether an object in the collection is equal to the parameter is determined using the
     * {@link Object#equals(Object)} method.
     *
     * @param value the object to be tested, may be {@code null}
     * @return {@code true} if the collection contains {@code value}, {@code false} otherwise
     */
    public boolean contains(Object value) {
        return false;
    }

    /**
     * Removes one occurrence of an object from the collection.
     *
     * Whether an object in the collection is equal to the parameter is determined using the
     * {@link Object#equals(Object)} method.
     *
     * @param value the object to be removed
     * @return {@code true} if an occurrence of {@code value} was found and removed, {@code false} otherwise
     *
     */
    public boolean remove(Object value) {
        return false;
    }

    /**
     * Creates an array from the collection.
     *
     * @return an array containing all objects contained in the collection; never {@code null}
     */
    public Object[] toArray() {
        throw new UnsupportedOperationException("toArray is not supported for the base Collection class.");
    }

    /**
     * Runs a {@link Processor}'s {@link Processor#process(Object)} method for every object in the collection.
     *
     * @param processor the {@link Processor} to use
     */
    public void forEach(Processor processor) {}

    /**
     * Adds all elements from another collection into itself.
     *
     * @param other the collection to add the elements from; remains unchanged
     */
    public void addAll(Collection other) {
        class AddProcessor extends Processor {
            public void process(Object value) {
                add(value);
            }
        }

        other.forEach(new AddProcessor());
    }

    /**
     * Removes all elements from the collection.
     */
    public void clear() {}
}
