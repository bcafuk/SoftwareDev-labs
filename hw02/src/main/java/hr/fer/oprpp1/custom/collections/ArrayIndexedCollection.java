package hr.fer.oprpp1.custom.collections;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * An array-backed collection with constant-time lookup.
 * <p>
 * This collection may contain duplicate elements, but not {@code null} references.
 *
 * @author Borna Cafuk
 */
public class ArrayIndexedCollection implements Collection {
    /**
     * The default capacity (i.e. the size of the internal array) when no capacity is specified in the constructor.
     */
    private static final int DEFAULT_CAPACITY = 16;
    /**
     * By how much the capacity is multiplied when an element is to be added to an already full array.
     */
    private static final int GROWTH_FACTOR = 2;

    /**
     * How many elements are contained in the collection, i.e. how many indices of the internal array are occupied.
     */
    private int size;
    /**
     * The internal array containing the elements of the collection. Unused indices are set to {@code null}.
     */
    private Object[] elements;

    /**
     * Constructs an array collection with the default initial capacity of
     * {@value ArrayIndexedCollection#DEFAULT_CAPACITY}.
     */
    public ArrayIndexedCollection() {
        this(DEFAULT_CAPACITY);
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
     * @throws NullPointerException if {@code other} is {@code null}
     */
    public ArrayIndexedCollection(Collection other) {
        this(other, 1);
    }

    /**
     * Constructs an array collection from the elements of another collection.
     * <p>
     * This constructor takes a minimum initial capacity. If the specified collection has more elements,
     * then the number of its elements is taken as the initial capacity instead.
     *
     * @param other           the collection whose elements to insert into the new array collection
     * @param initialCapacity the minimum initial capacity
     * @throws NullPointerException if {@code other} is {@code null}
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
     * @throws NullPointerException if the element is {@code null}
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
     * <p>
     * All elements that are currently at or after the position get shifted towards the end of the array.
     * Afterwards, the inserted element will have the specified index.
     *
     * @param value    the element to insert
     * @param position the position where to insert the element
     * @throws NullPointerException if the element is {@code null}
     */
    public void insert(Object value, int position) {
        Objects.requireNonNull(value, "null cannot be inserted into collection.");

        if (position < 0 || position > size)
            throw new IndexOutOfBoundsException("Valid positions are 0 to " + size + ", but " + position + " was passed.");

        growIfNecessary();

        if (position != size)
            System.arraycopy(elements, position, elements, position + 1, size - position);

        elements[position] = value;
        size++;
    }

    /**
     * Finds the first occurrence of an element in the array and returns its index.
     * <p>
     * Whether an object in the collection is equal to the parameter is determined using the
     * {@link Object#equals(Object)} method. The parameter may be {@code null}, in which case -1 is returned.
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
     * Whether an object in the collection is equal to the parameter is determined using the
     * {@link Object#equals(Object)} method.
     *
     * @param value the object to be removed
     * @return {@code true} if an occurrence of {@code value} was found and removed, {@code false} otherwise
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

        if (index != size - 1)
            System.arraycopy(elements, index + 1, elements, index, size - index - 1);
        size--;
        elements[size] = null;
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(elements, size);
    }

    /**
     * Runs a {@link Processor}'s {@link Processor#process(Object)} method for every object in the collection,
     * in order of ascending index.
     *
     * @param processor the {@link Processor} to use
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
     * Increases the size of the array {@value ArrayIndexedCollection#GROWTH_FACTOR} times if it is full.
     */
    private void growIfNecessary() {
        if (size < elements.length)
            return;

        Object[] newElements = new Object[elements.length * GROWTH_FACTOR];
        System.arraycopy(elements, 0, newElements, 0, size);
        elements = newElements;
    }

    /**
     * {@inheritDoc}
     * <p>
     * The elements are returned in order of increasing index.
     */
    @Override
    public ElementsGetter createElementsGetter() {
        return new Getter(this);
    }

    /**
     * An implementation of {@link ElementsGetter} for this class.
     */
    private static class Getter implements ElementsGetter {
        /**
         * The index of the first element which has not yet been returned by {@link #getNextElement()}.
         */
        private int currentIndex = 0;
        /**
         * The collection whose elements will be returned by this getter.
         * <p>
         * This could be removed by making {@link Getter} non-static,
         * but the assignment PDF specifies that it has to be static.
         */
        private ArrayIndexedCollection collection;

        /**
         * Constructs a new {@link Getter} for a given {@link ArrayIndexedCollection}.
         *
         * @param collection the collection whose elements will be returned by this getter
         */
        private Getter(ArrayIndexedCollection collection) {
            this.collection = collection;
        }

        @Override
        public boolean hasNextElement() {
            return currentIndex != collection.size;
        }

        @Override
        public Object getNextElement() {
            if (!hasNextElement())
                throw new NoSuchElementException("There are no more elements in this collection.");

            return collection.elements[currentIndex++];
        }
    }
}
