package hr.fer.oprpp1.custom.scripting.elems;

import java.util.Objects;

/**
 * An element representing a function name.
 *
 * @author Borna Cafuk
 */
public class ElementFunction extends Element {
    /**
     * The name of the function represented by the element.
     */
    private String name;

    /**
     * Initializes the element with a function name.
     *
     * @param name the function name with which to initialize the element
     */
    public ElementFunction(String name) {
        Objects.requireNonNull(name, "The function name must not be null.");
        this.name = name;
    }

    /**
     * Returns the function name.
     *
     * @return the function name
     */
    public String getName() {
        return name;
    }

    @Override
    public String asText() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof ElementFunction))
            return false;

        ElementFunction that = (ElementFunction) o;
        return name.equals(that.name);
    }
}
