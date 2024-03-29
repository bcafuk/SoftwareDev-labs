package hr.fer.oprpp1.custom.scripting.elems;

/**
 * Base class for the elements of expressions.
 *
 * @author Borna Cafuk
 */
public abstract class Element {
    /**
     * Converts the element into a string.
     *
     * @return the resulting string
     */
    public String asText() {
        return "";
    }

    @Override
    public String toString() {
        return asText();
    }
}
