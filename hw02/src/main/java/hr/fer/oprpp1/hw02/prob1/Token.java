package hr.fer.oprpp1.hw02.prob1;

/**
 * A {@link Lexer} token.
 *
 * @author Borna Cafuk
 */
public class Token {
    /**
     * The type of the token.
     */
    public enum TokenType {
        EOF,
        WORD,
        NUMBER,
        SYMBOL,
    }

    /**
     * The type of the token.
     */
    private TokenType type;
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
    public Token(TokenType type, Object value) {
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
    public TokenType getType() {
        return type;
    }
}
