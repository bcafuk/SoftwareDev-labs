package hr.fer.oprpp1.custom.scripting.elems;

import java.util.Objects;

/**
 * An element representing an operator.
 *
 * @author Borna Cafuk
 */
public class ElementOperator extends Element {
    /**
     * The operator represented by the element.
     */
    private String symbol;

    /**
     * Initializes the element with a symbol.
     *
     * @param symbol the symbol with which to initialize the element
     */
    public ElementOperator(String symbol) {
        Objects.requireNonNull(symbol, "The symbol must not be null.");
        this.symbol = symbol;
    }

    /**
     * Returns the symbol as a string.
     *
     * @return the symbol
     */
    public String getSymbol() {
        return symbol;
    }

    @Override
    public String asText() {
        return symbol;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof ElementOperator))
            return false;

        ElementOperator that = (ElementOperator) o;
        return symbol.equals(that.symbol);
    }
}
