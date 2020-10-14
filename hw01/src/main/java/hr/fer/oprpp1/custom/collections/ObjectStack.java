package hr.fer.oprpp1.custom.collections;

import java.util.Objects;

/**
 * A last in, first out stack for objects.
 * <p>
 * The stack is not allowed to contain {@code null}.
 *
 * @author Borna Cafuk
 */
public class ObjectStack {
    /**
     * The internal indexed collection that contains the stack elements.
     */
    private ArrayIndexedCollection collection;

    /**
     * Constructs a new empty stack.
     */
    public ObjectStack() {collection = new ArrayIndexedCollection();}

    /**
     * Returns whether the stack is empty.
     *
     * @return {@code true} if the stack is empty, {@code false} otherwise
     */
    public boolean isEmpty() {return collection.isEmpty();}

    /**
     * Returns the size of the stack.
     *
     * @return the number of objects currently stored on the stack
     */
    public int size() {return collection.size();}

    /**
     * Pushes an object onto the top of the stack.
     *
     * @param value the object to be added onto the stack
     * @throws NullPointerException if the object to be pushed is {@code null}
     */
    public void push(Object value) {
        Objects.requireNonNull(value, "null cannot be pushed onto the stack.");

        collection.add(value);
    }

    /**
     * Removes the object at the top of the stack and removes it.
     *
     * @return the object that was removed from the stack
     * @see ObjectStack#peek()
     */
    public Object pop() {
        if (isEmpty())
            throw new EmptyStackException();

        Object value = peek();
        collection.remove(collection.size() - 1);
        return value;
    }

    /**
     * Gets the object at the top of the stack, but does not remove it.
     *
     * @return the object at the top of the stack
     * @see ObjectStack#pop()
     */
    public Object peek() {return collection.get(collection.size() - 1);}

    /**
     * Removes all elements from the stack.
     */
    public void clear() {collection.clear();}
}
