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
        this.text = Objects.requireNonNull(text, "The text must not be null.");
    }

    /**
     * Gets the text represented by the node.
     *
     * @return the textual data
     */
    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (Character c : text.toCharArray()) {
            sb.append(switch (c) {
                case '{' -> "\\{";
                case '\\' -> "\\\\";
                default -> c;
            });
        }

        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof TextNode))
            return false;

        TextNode textNode = (TextNode) o;
        return text.equals(textNode.text);
    }
}
