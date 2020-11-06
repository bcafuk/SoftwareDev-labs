package hr.fer.zemris.lsystems.impl;

import hr.fer.oprpp1.custom.collections.ObjectStack;

/**
 * Represents a last-in, first-out stack of {@link TurtleState turtle states}.
 *
 * @author Borna Cafuk
 */
public class Context {
    /**
     * The underlying stack of states.
     */
    private ObjectStack<TurtleState> stateStack = new ObjectStack<>();

    /**
     * Gets the state currently at the top of the stack.
     *
     * @return the state currently at the top of the stack
     */
    public TurtleState getCurrentState() {
        return stateStack.peek();
    }

    /**
     * Adds a state to the top of the stack.
     *
     * @param state the state to be added to the stack
     */
    public void pushState(TurtleState state) {
        stateStack.push(state);
    }

    /**
     * Removes the state at the top of the stack from the stack.
     */
    public void popState() {
        stateStack.pop();
    }
}
