package hr.fer.oprpp1.custom.scripting.lexer;

import java.util.Objects;
import java.util.Set;

/**
 * Splits an input string into tokens according to the rules specified in the assignment document.
 *
 * @author Borna Cafuk
 */
public class SmartScriptLexer {
    /**
     * The first character which has not yet been consumed from the input.
     */
    private int currentIndex = 0;
    /**
     * The input to the lexer.
     */
    private char[] input;
    /**
     * The next token to be returned by {@link #getToken()}.
     */
    private Token next;
    /**
     * The current state of the lexer.
     */
    private LexerState state = LexerState.TEXT;

    /**
     * A set of characters which may be ecaped by a backslash while the lexer is in the {@link LexerState#TEXT} state.
     */
    private static final Set<Character> TEXT_ESCAPABLE = Set.of('\\', '{');

    /**
     * Constructs a new lexer for the given input.
     *
     * @param input the input string to tokenize
     * @throws NullPointerException if {@code input} is {@code null}
     */
    public SmartScriptLexer(String input) {
        Objects.requireNonNull(input, "The input must not be null.");

        this.input = input.toCharArray();
        next = lexToken();
    }

    /**
     * Puts the lexer into a given state.
     *
     * @param state the state to put the lexer into
     * @throws NullPointerException if {@code state} is {@code null}
     */
    public void setState(LexerState state) {
        Objects.requireNonNull(state, "The state must not be null.");
        this.state = state;
    }

    /**
     * Returns the next token without consuming it.
     *
     * @return the next token, or {@code null} if there are no more tokens
     */
    public Token peekToken() {
        return next;
    }

    /**
     * Consumes the next token and returns it.
     *
     * @return the consumed token
     * @throws LexerException if there are no more tokens
     */
    public Token getToken() {
        if (next == null)
            throw new LexerException("The input has been consumed ans there are no more tokens.");

        Token token = next;
        next = lexToken();
        return token;
    }

    /**
     * Returns the next token from the input, depending on the {@link #state}.
     *
     * @return the next token, or {@code null} if there are no more tokens
     */
    private Token lexToken() {
        if (currentIndex > input.length) {
            return null;
        }

        if (currentIndex == input.length) {
            currentIndex++;
            return new Token(TokenType.EOF, null);
        }

        return switch (state) {
            case TEXT -> lexTextToken();
            case TAG -> lexTagToken();
        };
    }

    /**
     * Returns the next token for the state {@link LexerState#TEXT}.
     *
     * @return the next token
     */
    private Token lexTextToken() {
        StringBuilder sb = new StringBuilder();

        if (isStringAt(currentIndex, "{$")) {
            currentIndex += 2;
            return new Token(TokenType.TAG_LEFT, null);
        }

        while (currentIndex != input.length && !isStringAt(currentIndex, "{$")) {
            if (input[currentIndex] != '\\') {
                sb.append(input[currentIndex++]);
                continue;
            }

            currentIndex++; // Skip the backslash

            if (!TEXT_ESCAPABLE.contains(input[currentIndex]))
                throw new LexerException("Invalid escape sequence: \\" + input[currentIndex]);

            sb.append(input[currentIndex++]);
        }

        return new Token(TokenType.BARE_STRING, sb.toString());
    }


    /**
     * Returns the next token for the state {@link LexerState#TAG}.
     *
     * @return the next token
     */
    private Token lexTagToken() {
        // TODO: Implement method
        return null;
    }

    /**
     * Checks if the input contains the given string at a given index.
     *
     * @param index    the index where the string is expected to begin
     * @param expected the string expected at the given index
     * @return {@code true} if the input contains the string at that position,
     *         {@code false} otherwise or if the index is out of bounds
     * @throws NullPointerException if {@code expected} is {@code null}
     */
    private boolean isStringAt(int index, String expected) {
        Objects.requireNonNull(expected, "The expected string must not be null.");

        for (int i = 0; i < expected.length(); i++) {
            if (index + i < 0 || index + i >= input.length)
                return false;

            if (input[index + 1] != expected.charAt(i))
                return false;
        }

        return true;
    }
}
