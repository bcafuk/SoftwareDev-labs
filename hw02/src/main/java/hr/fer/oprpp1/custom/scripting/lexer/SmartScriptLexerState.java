package hr.fer.oprpp1.custom.scripting.lexer;

/**
 * Represents the state of a {@link SmartScriptLexer}, i.e. the mode in which it interprets its input.
 *
 * @author Borna Cafuk
 */
public enum SmartScriptLexerState {
    TEXT,
    TAG,
}
