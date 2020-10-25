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
     * The input data.
     */
    private char[] data;
    /**
     * The current token, i.e. the last token returned by {@link #nextToken()}
     */
    private Token token = null;
    /**
     * The index of the first character which has not yet been handled.
     */
    private int currentIndex = 0;
    /**
     * The state of the lexer.
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

        this.data = input.toCharArray();
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
     * Gets the last token returned by {@link #nextToken()}.
     *
     * @return the last token processed from the input
     * @throws LexerException if {@link #nextToken()} has not yet been called
     */
    public Token getToken() {
        if (token == null)
            throw new LexerException("nextToken has not yet been called.");

        return token;
    }

    /**
     * Gets the next from the input token and returns it.
     * The same token will also be returned by subsequent calls to {@link #getToken()}.
     *
     * @return the token processed from the input
     * @throws LexerException if there is an error while getting the next token
     */
    public Token nextToken() {
        if (token != null && token.getType() == TokenType.EOF)
            throw new LexerException("The input string has already been consumed");

        token = switch (state) {
            case TEXT -> lexTextToken();
            case TAG -> lexTagToken();
        };

        return token;
    }

    /**
     * Gets the next from the input token using the rules for the {@link LexerState#TEXT} state
     * and returns it without storing it in {@link #token}.
     *
     * @return the token processed from the input
     * @throws LexerException if there is an error while getting the next token
     */
    private Token lexTextToken() {
        if (token != null && token.getType() == TokenType.EOF)
            throw new LexerException("The input string has already been consumed");

        if (currentIndex == data.length)
            return new Token(TokenType.EOF, null);

        if (isStringAt(currentIndex, "{$")) {
            currentIndex += 2;
            return new Token(TokenType.TAG_LEFT, null);
        }

        StringBuilder sb = new StringBuilder();

        while (currentIndex != data.length && !isStringAt(currentIndex, "{$")) {
            if (data[currentIndex] != '\\') {
                sb.append(data[currentIndex++]);
                continue;
            }


            //Escape sequence handling:
            currentIndex++; // Skip the backslash

            if (currentIndex == data.length)
                throw new LexerException("Invalid backslash at end of file, expected an escape sequence.");

            if (!TEXT_ESCAPABLE.contains(data[currentIndex]))
                throw new LexerException("Invalid escape sequence: \\" + data[currentIndex]);

            sb.append(data[currentIndex++]);
        }

        return new Token(TokenType.BARE_STRING, sb.toString());
    }


    /**
     * Gets the next from the input token using the rules for the {@link LexerState#TAG} state
     * and returns it without storing it in {@link #token}.
     *
     * @return the token processed from the input
     * @throws LexerException if there is an error while getting the next token
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
            if (index + i < 0 || index + i >= data.length)
                return false;

            if (data[index + i] != expected.charAt(i))
                return false;
        }

        return true;
    }
}
