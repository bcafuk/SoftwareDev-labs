package hr.fer.oprpp1.custom.scripting.lexer;

/**
 * A {@link SmartScriptLexer} token.
 *
 * @author Borna Cafuk
 */
public class SmartScriptToken {
    /**
     * The type of the token.
     */
    private SmartScriptTokenType type;
    /**
     * The value the token is holding.
     */
    private Object value;

    /**
     * Constructs a new token with the given type and value.
     *
     * @param type  the token's type
     * @param value the token's value; may be {@code null}
     */
    public SmartScriptToken(SmartScriptTokenType type, Object value) {
        this.type = type;
        this.value = value;
    }

    /**
     * Returns the token's value.
     *
     * @return the token's value
     */
    public Object getValue() {
        return value;
    }

    /**
     * Returns the token's type.
     *
     * @return the token's type
     */
    public SmartScriptTokenType getType() {
        return type;
    }
}
