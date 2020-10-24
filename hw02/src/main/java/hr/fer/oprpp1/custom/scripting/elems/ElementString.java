package hr.fer.oprpp1.custom.scripting.elems;

import java.util.Objects;

/**
 * An element representing a string.
 *
 * @author Borna Cafuk
 */
public class ElementString extends Element {
    /**
     * The string represented by the element.
     */
    private String value;

    /**
     * Initializes the element with a string.
     *
     * @param value the string with which to initialize the element
     */
    public ElementString(String value) {
        Objects.requireNonNull(value, "The string must not be null.");
        this.value = value;
    }

    /**
     * Returns the element's value.
     *
     * @return the element's value
     */
    public String getValue() {
        return value;
    }

    @Override
    public String asText() {
        return value;
    }
}
