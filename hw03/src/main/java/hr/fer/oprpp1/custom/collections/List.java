package hr.fer.oprpp1.custom.collections;

/**
 * A collection whose elements are ordered and have assigned indexes.
 *
 * @param <E> the type of objects to be stored in the list
 * @author Borna Cafuk
 */
public interface List<E> extends Collection<E> {
    /**
     * Gets the element at the specified index.
     *
     * @param index the index of the element to get
     * @return the element at the index
     * @throws IndexOutOfBoundsException if the index is less than 0 or if it is beyond the end of the list
     */
    E get(int index);

    /**
     * Inserts an element into the array at a specified position.
     * <p>
     * All elements that are currently at or after the position get shifted towards the end of the list.
     * Afterwards, the inserted element will have the specified index.
     * <p>
     * Can invalidate existing {@link ElementsGetter}s
     *
     * @param value    the element to insert
     * @param position the position where to insert the element
     * @throws NullPointerException if the element is {@code null}
     */
    void insert(E value, int position);

    /**
     * Finds the first occurrence of an element in the list and returns its index.
     * <p>
     * Whether an object in the collection is equal to the parameter is determined using the
     * {@link Object#equals(Object)} method. The parameter may be {@code null}, in which case -1 is returned.
     *
     * @param value the element to find
     * @return the index of the element if it exists in the collection, -1 otherwise
     */
    int indexOf(Object value);

    /**
     * Removes the element at the specified index.
     * All elements that are currently after the index get shifted towards the start of the list.
     * <p>
     * May invalidate existing {@link ElementsGetter}s.
     *
     * @param index the index at which to remove the element
     * @throws IndexOutOfBoundsException if the index is less than 0 or if it is beyond the end of the list
     */
    void remove(int index);
}
