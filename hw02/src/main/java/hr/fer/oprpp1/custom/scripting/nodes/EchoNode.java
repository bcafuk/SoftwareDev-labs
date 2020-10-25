package hr.fer.oprpp1.custom.scripting.nodes;

import hr.fer.oprpp1.custom.scripting.elems.Element;

import java.util.Objects;

/**
 * A node representing a command which generates textual output dynamically.
 */
public class EchoNode extends Node {
    /**
     * An array of elements to be output by this node.
     */
    private Element[] elements;

    /**
     * Creates a new echo node which outputs the given elements.
     *
     * @param elements the elements to be output by the node
     * @throws NullPointerException if {@code elements} is {@code null}.
     */
    public EchoNode(Element[] elements) {
        Objects.requireNonNull(elements, "The elements array must not be null");

        this.elements = elements;
    }

    /**
     * Returns the elements which this node outputs.
     *
     * @return an array of elements to be output by this node
     */
    public Element[] getElements() {
        return elements;
    }
}
