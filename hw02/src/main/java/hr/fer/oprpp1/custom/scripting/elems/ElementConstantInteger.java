package hr.fer.oprpp1.custom.scripting.elems;

/**
 * An element representing a constant integer.
 *
 * @author Borna Cafuk
 */
public class ElementConstantInteger extends Element {
    /**
     * The value represented by the element.
     */
    private int value;

    /**
     * Initializes the element with a value.
     *
     * @param value the value with which to initialize the element
     */
    public ElementConstantInteger(int value) {
        this.value = value;
    }

    /**
     * Returns the element's value.
     *
     * @return the element's value
     */
    public int getValue() {
        return value;
    }

    @Override
    public String asText() {
        return Integer.toString(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof ElementConstantInteger))
            return false;

        ElementConstantInteger that = (ElementConstantInteger) o;
        return value == that.value;
    }
}
