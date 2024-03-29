package hr.fer.oprpp1.custom.scripting.lexer;

/**
 * The type of a {@link SmartScriptLexer} token.
 *
 * @author Borna Cafuk
 */
public enum SmartScriptTokenType {
    EOF,
    BARE_STRING,
    TAG_LEFT,
    TAG_RIGHT,
    IDENTIFIER,
    EQUALS,
    STRING,
    DOUBLE,
    INTEGER,
    FUNCTION,
    OPERATOR,
}
