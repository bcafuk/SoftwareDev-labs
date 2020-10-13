package hr.fer.oprpp1.custom.collections;

/**
 * A generic collection of objects.
 *
 * @author Borna Cafuk
 */
public class Collection {
    protected Collection() {}

    /**
     * Returns whether the collection is empty.
     *
     * @return <code>true</code> if the collection is empty, <code>false</code> otherwise
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
     * Whether an object in the collection is equal to the parameter is determined using the <code>equals</code> method.
     *
     * @param value the object to be tested, may be <code>null</code>
     * @return <code>true</code> if the collection contains <code>value</code>, <code>false</code> otherwise
     */
    public boolean contains(Object value) {
        return false;
    }

    /**
     * Removes one occurrence of an object from the collection.
     * Whether an object in the collection is equal to the parameter is determined using the <code>equals</code> method.
     *
     * @param value the object to be removed
     * @return <code>true</code> if an occurrence of <code>value</code> was found and removed, <code>false</code> otherwise
     */
    public boolean remove(Object value) {
        return false;
    }

    /**
     * Creates an array from the collection.
     *
     * @return an array containing all objects contained in the collection; never <code>null</code>
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
