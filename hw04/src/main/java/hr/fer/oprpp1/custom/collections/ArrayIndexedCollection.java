package hr.fer.oprpp1.custom.collections;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * An array-backed collection with constant-time lookup.
 * <p>
 * This collection may contain duplicate elements, but not {@code null} references.
 *
 * @param <E> the type of objects to be stored in the collection
 * @author Borna Cafuk
 */
public class ArrayIndexedCollection<E> implements List<E> {
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
    private E[] elements;
    /**
     * A modification counter used in {@link ArrayIndexedElementsGetter} to check for concurrent modifications.
     */
    private long modificationCount = 0;

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
    @SuppressWarnings("unchecked")
    public ArrayIndexedCollection(int initialCapacity) {
        if (initialCapacity < 1)
            throw new IllegalArgumentException("The initial size of the collection must be at least 1, but " + initialCapacity + "was given.");

        size = 0;
        elements = (E[]) new Object[initialCapacity];
    }

    /**
     * Constructs an array collection from the elements of another collection.
     *
     * @param other the collection whose elements to insert into the new array collection
     * @throws NullPointerException if {@code other} is {@code null}
     */
    public ArrayIndexedCollection(Collection<? extends E> other) {
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
    public ArrayIndexedCollection(Collection<? extends E> other, int initialCapacity) {
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
     * <p>
     * Invalidates existing {@link ArrayIndexedElementsGetter}s if a reallocation occurs.
     *
     * @param value the element to add
     * @throws NullPointerException if the element is {@code null}
     */
    @Override
    public void add(E value) {
        insert(value, size);
    }

    @Override
    public E get(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Valid indices are 0 to " + (size - 1) + ", but " + index + " was passed.");

        return elements[index];
    }

    @Override
    public void insert(E value, int position) {
        Objects.requireNonNull(value, "null cannot be inserted into collection.");

        if (position < 0 || position > size)
            throw new IndexOutOfBoundsException("Valid positions are 0 to " + size + ", but " + position + " was passed.");

        growIfNecessary();

        if (position != size) {
            System.arraycopy(elements, position, elements, position + 1, size - position);
            modificationCount++;
        }

        elements[position] = value;
        size++;
    }

    @Override
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
     * <p>
     * Invalidates existing {@link ArrayIndexedElementsGetter}s if the element removed is not from the end.
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

    @Override
    public void remove(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Valid indices are 0 to " + (size - 1) + ", but " + index + " was passed.");

        if (index != size - 1) {
            System.arraycopy(elements, index + 1, elements, index, size - index - 1);
            modificationCount++;
        }

        size--;
        elements[size] = null;
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(elements, size);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Invalidates existing {@link ArrayIndexedElementsGetter}s.
     */
    @Override
    public void clear() {
        size = 0;
        Arrays.fill(elements, null);
        modificationCount++;
    }

    /**
     * Increases the size of the array {@value ArrayIndexedCollection#GROWTH_FACTOR} times if it is full.
     * <p>
     * Invalidates existing {@link ArrayIndexedElementsGetter}s if a reallocation occurs.
     */
    @SuppressWarnings("unchecked")
    private void growIfNecessary() {
        if (size < elements.length)
            return;

        E[] newElements = (E[]) new Object[elements.length * GROWTH_FACTOR];
        System.arraycopy(elements, 0, newElements, 0, size);
        elements = newElements;
        modificationCount++;
    }

    /**
     * {@inheritDoc}
     * <p>
     * The elements are returned in order of increasing index.
     */
    @Override
    public ElementsGetter<E> createElementsGetter() {
        return new ArrayIndexedElementsGetter<>(this);
    }

    /**
     * An implementation of {@link ElementsGetter} for this class.
     *
     * @param <E> the type of the elements
     */
    private static class ArrayIndexedElementsGetter<E> implements ElementsGetter<E> {
        /**
         * The index of the first element which has not yet been returned by {@link #getNextElement()}.
         */
        private int currentIndex = 0;
        /**
         * The collection whose elements will be returned by this getter.
         * <p>
         * This could be removed by making {@link ArrayIndexedElementsGetter} non-static,
         * but the assignment PDF specifies that it has to be static.
         */
        private ArrayIndexedCollection<E> collection;
        /**
         * The {@link #modificationCount} at the moment of this {@link ElementsGetter}'s creation.
         * <p>
         * This is used to monitor for concurrent modifications.
         */
        private long savedModificationCount;

        /**
         * Constructs a new {@link ArrayIndexedElementsGetter} for a given {@link ArrayIndexedCollection}.
         *
         * @param collection the collection whose elements will be returned by this getter
         */
        private ArrayIndexedElementsGetter(ArrayIndexedCollection<E> collection) {
            this.collection = collection;
            this.savedModificationCount = collection.modificationCount;
        }

        @Override
        public boolean hasNextElement() {
            if (collection.modificationCount != savedModificationCount)
                throw new ConcurrentModificationException("The collection has been modified since the ElementsGetter has been constructed.");

            return currentIndex != collection.size;
        }

        @Override
        public E getNextElement() {
            if (!hasNextElement())
                throw new NoSuchElementException("There are no more elements in this collection.");

            return collection.elements[currentIndex++];
        }
    }
}
