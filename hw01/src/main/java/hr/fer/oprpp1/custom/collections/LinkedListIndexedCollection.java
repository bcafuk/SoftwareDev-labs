package hr.fer.oprpp1.custom.collections;

import java.util.Objects;

/**
 * A linked-list-backed collection.
 * This collection may contain duplicate elements, but not <code>null</code> references.
 *
 * @author Borna Cafuk
 */
public class LinkedListIndexedCollection extends Collection {
    private static class ListNode {
        public ListNode next;
        public ListNode previous;
        public Object value;

        public ListNode(ListNode next, ListNode previous, Object value) {
            this.next = next;
            this.previous = previous;
            this.value = value;
        }
    }

    private int size;
    private ListNode first;
    private ListNode last;

    /**
     * Constructs empty collection.
     */
    public LinkedListIndexedCollection() {
        size = 0;
        first = null;
        last = null;
    }

    /**
     * Constructs an array collection from the elements of another collection.
     *
     * @param other the collection whose elements to insert into the new linked list collection
     * @throws NullPointerException if <code>other</code> is <code>null</code>
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
     * @throws NullPointerException if the element is <code>null</code>
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
     * Gets the <code>ListNode</code> at the specified index.
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
    }

    /**
     * Inserts an element into the list at a specified position.
     * All elements that are currently at or after the position get shifted towards the end of the list.
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
    }

    /**
     * Finds the first occurrence of an element in the list and returns its index.
     * Whether an object in the collection is equal to the parameter is determined using the <code>equals</code> method.
     * The parameter may be <code>null</code>, in which case -1 is returned.
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
     * Finds the first node in the list whose value is equal to the parameter.
     * Whether an object in the collection is equal to the parameter is determined using the <code>equals</code> method.
     * The parameter may be <code>null</code>, in which case <code>null</code> is returned.
     *
     * @param value the element to find
     * @return the node with the value if it exists in the collection, <code>null</code> otherwise
     */
    public ListNode findNode(Object value) {
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
     * Whether an object in the collection is equal to the parameter is determined using the <code>equals</code> method.
     *
     * @param value the object to be removed
     * @return <code>true</code> if an occurrence of <code>value</code> was found and removed, <code>false</code> otherwise
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
     * Runs a processor's <code>process</code> method for every object in the collection, in order of ascending index.
     *
     * @param processor the processor to use
     */
    @Override
    public void forEach(Processor processor) {
        for (ListNode node = first; node != null; node = node.next)
            processor.process(node.value);
    }
}
