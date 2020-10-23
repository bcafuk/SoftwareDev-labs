package hr.fer.oprpp1.custom.collections;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * A linked-list-backed collection.
 * <p>
 * This collection may contain duplicate elements, but not {@code null} references.
 *
 * @author Borna Cafuk
 */
public class LinkedListIndexedCollection implements Collection {
    /**
     * An internal class representing a node of a linked list.
     */
    private static class ListNode {
        /**
         * Reference to the next node in the list, or {@code null} if this node is the last in the list.
         */
        public ListNode next;
        /**
         * Reference to the previous node in the list, or {@code null} if this node is the first in the list.
         */
        public ListNode previous;
        /**
         * The payload of the node, the object represented by this node.
         */
        public Object value;

        /**
         * Initializes a list node with all its fields.
         *
         * @param next     the node in the list immediately following this one
         * @param previous the node in the list immediately preceding this one
         * @param value    the object represented by this node
         */
        public ListNode(ListNode next, ListNode previous, Object value) {
            this.next = next;
            this.previous = previous;
            this.value = value;
        }
    }

    /**
     * The number of elements currently contained in the linked list.
     */
    private int size;
    /**
     * The first node in the list, or {@code null} if the list is empty.
     */
    private ListNode first;
    /**
     * The last node in the list, or {@code null} if the list is empty.
     */
    private ListNode last;
    /**
     * A modification counter used in {@link Getter} to check for concurrent modifications.
     */
    private long modificationCount = 0;

    /**
     * Constructs an empty collection.
     */
    public LinkedListIndexedCollection() {
        size = 0;
        first = null;
        last = null;
    }

    /**
     * Constructs a linked list collection from the elements of another collection.
     *
     * @param other the collection whose elements to insert into the new linked list collection
     * @throws NullPointerException if {@code other} is {@code null}
     */
    public LinkedListIndexedCollection(Collection other) {
        this();

        addAll(other);
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
     * Adds an element to the end of the list.
     *
     * @param value the element to add
     * @throws NullPointerException if the element is {@code null}
     */
    @Override
    public void add(Object value) {
        Objects.requireNonNull(value, "null cannot be added to collection.");

        ListNode newNode = new ListNode(null, last, value);

        if (last != null)
            last.next = newNode;

        if (first == null)
            first = newNode;
        last = newNode;

        size++;
        modificationCount++;
    }

    /**
     * Gets the element at the specified index.
     *
     * @param index the index of the element to get
     * @return the element at the index
     * @throws IndexOutOfBoundsException if the index is less than 0 or if it is beyond the end of the list
     */
    public Object get(int index) {
        return getNode(index).value;
    }

    /**
     * Gets the node at the specified index.
     *
     * @param index the index of the element to get
     * @return the element at the index
     * @throws IndexOutOfBoundsException if the index is less than 0 or if it is beyond the end of the list
     */
    private ListNode getNode(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Valid indices are 0 to " + (size - 1) + ", but " + index + " was passed.");

        ListNode currentNode;
        if (index <= size / 2) {
            currentNode = last;
            for (int currentIndex = size - 1; currentIndex > index; currentIndex--)
                currentNode = currentNode.previous;
        } else {
            currentNode = first;
            for (int currentIndex = 0; currentIndex < index; currentIndex++)
                currentNode = currentNode.next;
        }
        return currentNode;
    }

    @Override
    public void clear() {
        size = 0;
        first = null;
        last = null;
        modificationCount++;
    }

    /**
     * Inserts an element into the list at a specified position.
     * <p>
     * All elements that are currently at or after the position get shifted towards the end of the list.
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

        if (position == size) {
            add(value);
            return;
        }

        ListNode currentNode = getNode(position);
        ListNode newNode = new ListNode(currentNode, currentNode.previous, value);

        if (currentNode.previous != null)
            currentNode.previous.next = newNode;
        else
            first = newNode;

        currentNode.previous = newNode;

        size++;
        modificationCount++;
    }

    /**
     * Finds the first occurrence of an element in the list and returns its index.
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

        int index = 0;
        for (ListNode node = first; node != null; node = node.next) {
            if (value.equals(node.value))
                return index;
            index++;
        }

        return -1;
    }

    /**
     * Finds the first node in the list whose {@link ListNode#value} is equal to the parameter.
     * <p>
     * Whether an object in the collection is equal to the parameter is determined using the
     * {@link Object#equals(Object)} method. The parameter may be {@code null}, in which case {@code null} is returned.
     *
     * @param value the element to find
     * @return the node with the value if it exists in the collection, {@code null} otherwise
     */
    private ListNode findNode(Object value) {
        if (value == null)
            return null;

        for (ListNode node = first; node != null; node = node.next) {
            if (value.equals(node.value))
                return node;
        }

        return null;
    }

    /**
     * Removes the first occurrence of an object from the collection.
     * <p>
     * Whether an object in the collection is equal to the parameter is determined using the
     * {@link Object#equals(Object)} method.
     *
     * @param value the object to be removed
     * @return {@code true} if an occurrence of {@code value} was found and removed, {@code false} otherwise
     */
    @Override
    public boolean remove(Object value) {
        ListNode node = findNode(value);

        if (node == null)
            return false;

        remove(node);
        return true;
    }

    /**
     * Removes the element at the specified index.
     * <p>
     * All elements that are currently after the index get shifted towards the start of the array.
     *
     * @param index the index at which to remove the element
     * @throws IndexOutOfBoundsException if the index is less than 0 or if it is beyond the end of the list
     */
    public void remove(int index) {
        remove(getNode(index));
    }

    /**
     * Removes a node from the list.
     *
     * @param node the node to remove
     */
    private void remove(ListNode node) {
        if (node.previous != null)
            node.previous.next = node.next;
        else
            first = node.next;

        if (node.next != null)
            node.next.previous = node.previous;
        else
            last = node.previous;

        size--;
        modificationCount++;
    }

    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];

        int index = 0;
        for (ListNode node = first; node != null; node = node.next) {
            array[index] = node.value;
            index++;
        }

        return array;
    }

    /**
     * Runs a {@link Processor}'s {@link Processor#process(Object)} method for every object in the collection,
     * in order of ascending index.
     *
     * @param processor the {@link Processor} to use
     */
    @Override
    public void forEach(Processor processor) {
        for (ListNode node = first; node != null; node = node.next)
            processor.process(node.value);
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
         * The node of the first element which has not yet been returned by {@link #getNextElement()}.
         */
        private ListNode currentNode;
        /**
         * The collection whose elements will be returned by this getter.
         * <p>
         * This could be removed by making {@link Getter} non-static,
         * but the assignment PDF specifies that it has to be static.
         */
        private LinkedListIndexedCollection collection;
        /**
         * The {@link #modificationCount} at the moment of this {@link ElementsGetter}'s creation.
         * <p>
         * This is used to monitor for concurrent modifications.
         */
        private long savedModificationCount;

        /**
         * Constructs a new {@link Getter} for a given {@link LinkedListIndexedCollection}.
         *
         * @param collection the collection whose elements will be returned by this getter
         */
        private Getter(LinkedListIndexedCollection collection) {
            this.collection = collection;
            this.currentNode = collection.first;
            this.savedModificationCount = collection.modificationCount;
        }

        @Override
        public boolean hasNextElement() {
            if (collection.modificationCount != savedModificationCount)
                throw new ConcurrentModificationException("The collection has been modified since the ElementsGetter has been constructed.");

            return currentNode != null;
        }

        @Override
        public Object getNextElement() {
            if (!hasNextElement())
                throw new NoSuchElementException("There are no more elements in this collection.");

            Object value = currentNode.value;
            currentNode = currentNode.next;
            return value;
        }
    }
}
