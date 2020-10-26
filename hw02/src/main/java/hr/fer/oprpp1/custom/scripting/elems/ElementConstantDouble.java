package hr.fer.oprpp1.custom.scripting.elems;

import java.util.Objects;

/**
 * An element representing a constant real number.
 *
 * @author Borna Cafuk
 */
public class ElementConstantDouble extends Element {
    /**
     * The value represented by the element.
     */
    private double value;

    /**
     * Initializes the element with a value.
     *
     * @param value the value with which to initialize the element
     */
    public ElementConstantDouble(double value) {
        this.value = value;
    }

    /**
     * Returns the element's value.
     *
     * @return the element's value
     */
    public double getValue() {
        return value;
    }

    @Override
    public String asText() {
        return Double.toString(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof ElementConstantDouble))
            return false;

        ElementConstantDouble that = (ElementConstantDouble) o;
        return Double.compare(that.value, value) == 0;
    }
}
