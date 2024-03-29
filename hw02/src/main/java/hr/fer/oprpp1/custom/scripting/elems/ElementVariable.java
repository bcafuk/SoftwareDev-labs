package hr.fer.oprpp1.custom.scripting.elems;

import java.util.Objects;

/**
 * An element representing a variable name.
 *
 * @author Borna Cafuk
 */
public class ElementVariable extends Element {
    /**
     * The name of the variable represented by the element.
     */
    private String name;

    /**
     * Initializes the element with a variable name.
     *
     * @param name the variable name with which to initialize the element
     */
    public ElementVariable(String name) {
        this.name = Objects.requireNonNull(name, "The variable name must not be null.");
    }

    /**
     * Returns the variable name.
     *
     * @return the variable name
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
        if (!(o instanceof ElementVariable))
            return false;

        ElementVariable that = (ElementVariable) o;
        return name.equals(that.name);
    }
}
