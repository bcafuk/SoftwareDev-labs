package hr.fer.oprpp1.custom.collections;

import java.util.Arrays;
import java.util.Objects;

/**
 * An array-backed collection with constant-time lookup.
 * This collection may contain duplicate elements, but not <code>null</code> references.
 *
 * @author Borna Cafuk
 */
public class ArrayIndexedCollection extends Collection {
    private static final int defaultCapacity = 16;
    private static final int growthFactor = 2;

    private int size;
    private Object[] elements;

    /**
     * Constructs an array collection with the default initial capacity of 16.
     */
    public ArrayIndexedCollection() {
        this(defaultCapacity);
    }

    /**
     * Constructs an array collection with the specified initial capacity.
     *
     * @param initialCapacity the initial capacity; must be 1 or greater
     * @throws IllegalArgumentException if the specified initial capacity is less than 1
     */
    public ArrayIndexedCollection(int initialCapacity) {
        if (initialCapacity < 1)
            throw new IllegalArgumentException("The initial size of the collection must be at least 1, but " + initialCapacity + "was given.");

        size = 0;
        elements = new Object[initialCapacity];
    }

    /**
     * Constructs an array collection from the elements of another collection.
     *
     * @param other the collection whose elements to insert into the new array collection
     */
    public ArrayIndexedCollection(Collection other) {
        this(other, defaultCapacity);
    }

    /**
     * Constructs an array collection from the elements of another collection.
     * It also takes a minimum initial capacity. If the specified collection has more elements,
     * then the number of its elements is taken as the initial capacity instead.
     *
     * @param other           the collection whose elements to insert into the new array collection
     * @param initialCapacity the minimum initial capacity
     */
    public ArrayIndexedCollection(Collection other, int initialCapacity) {
        this(Math.max(other.size(), initialCapacity));

        this.addAll(other);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean contains(Object value) {
        return indexOf(value) != -1;
    }

    /**
     * Adds an element to the end of the array.
     *
     * @param value the element to add
     * @throws NullPointerException if the element is <code>null</code>
     */
    @Override
    public void add(Object value) {
        insert(value, size);
    }

    /**
     * Gets the element at the specified index.
     *
     * @param index the index of the element to get
     * @return the element at the index
     * @throws IndexOutOfBoundsException if the index is less than 0 or if it is beyond the end of the array
     */
    public Object get(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Valid indices are 0 to " + (size - 1) + ", but " + index + " was passed.");

        return elements[index];
    }

    /**
     * Inserts an element into the array at a specified position.
     * All elements that are currently at or after the position get shifted towards the end of the array.
     * Afterwards, the inserted element will have the specified index.
     *
     * @param value    the element to insert
     * @param position the position where to insert the element
     * @throws NullPointerException if the element is <code>null</code>
     */
    public void insert(Object value, int position) {
        Objects.requireNonNull(value, "null cannot be inserted into collection.");

        if (position < 0 || position > size)
            throw new IndexOutOfBoundsException("Valid positions are 0 to " + size + ", but " + position + " was passed.");

        growIfNecessary();
        System.arraycopy(elements, position, elements, position + 1, size - position);
        elements[position] = value;
        size++;
    }

    /**
     * Finds the first occurrence of an element in the array and returns its index.
     * Whether an object in the collection is equal to the parameter is determined using the <code>equals</code> method.
     * The parameter may be <code>null</code>, in which case -1 is returned.
     *
     * @param value the element to find
     * @return the index of the element if it exists in the collection, -1 otherwise
     */
    public int indexOf(Object value) {
        if (value == null)
            return -1;

        for (int i = 0; i < size; i++)
            if (value.equals(elements[i]))
                return i;

        return -1;
    }

    /**
     * Removes the first occurrence of an object from the collection.
     * Whether an object in the collection is equal to the parameter is determined using the <code>equals</code> method.
     *
     * @param value the object to be removed
     * @return <code>true</code> if an occurrence of <code>value</code> was found and removed, <code>false</code> otherwise
     */
    @Override
    public boolean remove(Object value) {
        int index = indexOf(value);

        if (index == -1)
            return false;

        remove(index);
        return true;
    }

    /**
     * Removes the element at the specified index.
     * All elements that are currently after the index get shifted towards the start of the array.
     *
     * @param index the index at which to remove the element
     * @throws IndexOutOfBoundsException if the index is less than 0 or if it is beyond the end of the array
     */
    public void remove(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Valid indices are 0 to " + (size - 1) + ", but " + index + " was passed.");

        System.arraycopy(elements, index + 1, elements, index, size - index - 1);
        size--;
        elements[size] = null;
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(elements, size);
    }

    /**
     * Runs a processor's <code>process</code> method for every object in the collection, in order of ascending index.
     *
     * @param processor the processor to use
     */
    @Override
    public void forEach(Processor processor) {
        for (int i = 0; i < size; i++)
            processor.process(elements[i]);
    }

    @Override
    public void clear() {
        size = 0;
        Arrays.fill(elements, null);
    }

    /**
     * Doubles the size of the array if it is full.
     */
    private void growIfNecessary() {
        if (size < elements.length)
            return;

        Object[] newElements = new Object[elements.length * growthFactor];
        System.arraycopy(elements, 0, newElements, 0, size);
        elements = newElements;
    }
}
