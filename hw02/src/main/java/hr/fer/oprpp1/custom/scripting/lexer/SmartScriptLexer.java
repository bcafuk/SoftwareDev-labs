package hr.fer.oprpp1.custom.scripting.lexer;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

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
     * A set of characters which unambiguously represent operators in the {@link LexerState#TAG} state.
     * The minus can also be the start of a number, so it is not in this set.
     */
    private static final Set<Character> TAG_OPERATORS = Set.of('+', '*', '/', '^');

    /**
     * A map of escape sequences valid in strings in the {@link LexerState#TAG} state.
     */
    private static final Map<Character, Character> TAG_STRING_ESCAPES = Map.of(
            '\\', '\\',
            '"', '"',
            'n', '\n',
            'r', '\r',
            't', '\t'
    );

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
        while (currentIndex < data.length && Character.isWhitespace(data[currentIndex]))
            currentIndex++;

        if (currentIndex == data.length)
            return new Token(TokenType.EOF, null);

        if (isStringAt(currentIndex, "$}")) {
            currentIndex += 2;
            return new Token(TokenType.TAG_RIGHT, null);
        }

        if (data[currentIndex] == '=') {
            currentIndex++; // Consume the equals sign
            return new Token(TokenType.EQUALS, null);
        }

        if (data[currentIndex] == '@') {
            currentIndex++; // Skip the @
            return new Token(TokenType.FUNCTION, consumeIdentifier());
        }

        if (isIdentifierStart(data[currentIndex])) {
            return new Token(TokenType.IDENTIFIER, consumeIdentifier());
        }

        if (TAG_OPERATORS.contains(data[currentIndex]))
            return new Token(TokenType.OPERATOR, data[currentIndex++]);

        if (data[currentIndex] == '-') {
            if (currentIndex + 1 != data.length && Character.isDigit(data[currentIndex + 1]))
                return new Token(TokenType.NUMBER, consumeNumber());

            currentIndex++; // Skip the -
            return new Token(TokenType.OPERATOR, '-');
        }

        if (Character.isDigit(data[currentIndex]))
            return new Token(TokenType.NUMBER, consumeNumber());

        if (data[currentIndex] == '"')
            return new Token(TokenType.STRING, consumeString());

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

    /**
     * Tests whether a character may be found at the start of an identifier.
     * <p>
     * Valid characters for the start of an identifier are letters and underscores ({@code _}).
     *
     * @param c the character to test
     * @return {@code true} if {@code c} is valid at the start of an identifier, {@code false} otherwise.
     */
    private static boolean isIdentifierStart(Character c) {
        return Character.isLetter(c) || c == '_';
    }

    /**
     * Tests whether a character may be found in an identifier.
     * <p>
     * Valid characters for an identifier are letters, digits and underscores ({@code _}).
     *
     * @param c the character to test
     * @return {@code true} if {@code c} is valid in an identifier, {@code false} otherwise.
     */
    private static boolean isIdentifierPart(Character c) {
        return Character.isLetterOrDigit(c) || c == '_';
    }

    /**
     * Reads in an identifier from the input.
     *
     * @return the identifier string
     * @throws LexerException if the input has been consumed entirely
     * @throws LexerException if next character in the input cannot start an identifier
     */
    private String consumeIdentifier() {
        if (currentIndex == data.length)
            throw new LexerException("Tried reading an identifier at the end of the file.");

        if (!isIdentifierStart(data[currentIndex]))
            throw new LexerException("Tried reading an identifier, found " + data[currentIndex]);

        StringBuilder sb = new StringBuilder();

        while (currentIndex < data.length && isIdentifierPart(data[currentIndex]))
            sb.append(data[currentIndex++]);

        return sb.toString();
    }

    /**
     * Reads in a number from the input.
     *
     * @return the number
     * @throws LexerException if the input has been consumed entirely
     * @throws LexerException if the number is incorrectly formatted
     */
    private double consumeNumber() {
        StringBuilder sb = new StringBuilder();

        if (currentIndex == data.length)
            throw new LexerException("Tried reading a number at the end of the file.");

        if (data[currentIndex] == '-') {
            currentIndex++;
            sb.append('-');
        }

        boolean periodFound = false;
        while (currentIndex < data.length) {
            if (data[currentIndex] == '.') {
                if (periodFound)
                    break;
                periodFound = true;
            } else if (!Character.isDigit(data[currentIndex])) {
                break;
            }

            sb.append(data[currentIndex++]);
        }

        try {
            return Double.parseDouble(sb.toString());
        } catch (NumberFormatException e) {
            throw new LexerException("Invalid number: " + sb.toString(), e);
        }
    }

    /**
     * Reads in a string from the input.
     *
     * @return the string contents, without the surrounding quotation marks
     * @throws LexerException if the input has been consumed entirely
     * @throws LexerException if the next character in the input is not a quotation mark ({@code "})
     * @throws LexerException if the string is unterminated
     * @throws LexerException if there is an invalid escape sequence in the string
     */
    private String consumeString() {
        if (currentIndex == data.length)
            throw new LexerException("Tried reading a string at the end of the file.");

        if (data[currentIndex] != '"')
            throw new LexerException("Tried reading a string, but found " + data[currentIndex] + " instead of a quotation mark (\").");

        currentIndex++; // Skip the opening quotation mark;

        StringBuilder sb = new StringBuilder();

        while (currentIndex < data.length && data[currentIndex] != '"') {
            if (data[currentIndex] != '\\') {
                sb.append(data[currentIndex++]);
                continue;
            }

            //Escape sequence handling:
            currentIndex++; // Skip the backslash

            if (currentIndex == data.length)
                throw new LexerException("Unterminated string with backslash at end of file.");

            if (!TAG_STRING_ESCAPES.containsKey(data[currentIndex]))
                throw new LexerException("Invalid escape sequence: \\" + data[currentIndex]);

            sb.append(TAG_STRING_ESCAPES.get(data[currentIndex++]));
        }

        if (currentIndex == data.length)
            throw new LexerException("Unterminated string in input.");

        currentIndex++; // Skip the closing quotation mark;

        return sb.toString();
    }
}
