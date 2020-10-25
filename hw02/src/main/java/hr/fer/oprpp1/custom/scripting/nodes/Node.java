package hr.fer.oprpp1.custom.scripting.nodes;

import hr.fer.oprpp1.custom.collections.ArrayIndexedCollection;

import java.util.Objects;

/**
 * Base class for the nodes in a syntax tree.
 *
 * @author Borna Cafuk
 */
public abstract class Node {
    /**
     * The node's children in the syntax tree.
     * <p>
     * This collection is lazily constructed. It will be {@code null} until the first child is added.
     */
    private ArrayIndexedCollection children = null;

    /**
     * Adds a child node.
     *
     * @param child the node to add as a child
     * @throws NullPointerException if {@code child} is {@code null}
     */
    public void addChildNode(Node child) {
        Objects.requireNonNull(child, "The child node must not be null");

        if (children == null)
            children = new ArrayIndexedCollection();

        children.add(child);
    }

    /**
     * Gets the number of child nodes.
     *
     * @return the number of child nodes
     */
    public int numberOfChildren() {
        if (children == null)
            return 0;

        return children.size();
    }

    /**
     * Gets a child node by its index.
     *
     * @param index the index from which to retrieve the child node
     * @return the child node
     * @throws IndexOutOfBoundsException if the index is negative or beyond the end of the list of children
     */
    public Node getChild(int index) {
        if (children == null)
            throw new IndexOutOfBoundsException("No children have been added to this node.");

        return (Node) children.get(index);
    }
}
