package hr.fer.oprpp1.custom.scripting.nodes;

import hr.fer.oprpp1.custom.scripting.elems.Element;

import java.util.Arrays;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{$ = ");

        for (Element element : elements)
            sb.append(element).append(' ');

        sb.append("$}");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof EchoNode))
            return false;

        EchoNode echoNode = (EchoNode) o;
        return Arrays.equals(elements, echoNode.elements);
    }
}
