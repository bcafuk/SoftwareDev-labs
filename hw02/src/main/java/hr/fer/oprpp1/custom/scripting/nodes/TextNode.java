package hr.fer.oprpp1.custom.scripting.nodes;

import java.util.Objects;

/**
 * A node representing a piece of textual data.
 *
 * @author Borna Cafuk
 */
public class TextNode extends Node {
    /**
     * The text represented by this node.
     */
    private String text;

    /**
     * Constructs a new text node.
     *
     * @param text the data to be represented by the node
     * @throws NullPointerException if {@code text} is {@code null}
     */
    public TextNode(String text) {
        Objects.requireNonNull(text, "The text must not be null.");

        this.text = text;
    }

    /**
     * Gets the text represented by the node.
     *
     * @return the textual data
     */
    public String getText() {
        return text;
    }
}
