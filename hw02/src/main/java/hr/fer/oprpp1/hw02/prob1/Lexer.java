package hr.fer.oprpp1.hw02.prob1;

import java.util.Objects;

/**
 * Splits an input string into tokens according to the rules specified in the assignment document.
 *
 * @author Borna Cafuk
 */
public class Lexer {
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
    private LexerState state = LexerState.BASIC;

    /**
     * Creates a lexer for a given string.
     *
     * @param text the text which will be tokenized by this lexer
     * @throws NullPointerException if {@code text} is {@code null}
     */
    public Lexer(String text) {
        Objects.requireNonNull(text, "The input text must not be null.");

        data = text.toCharArray();
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

        while (currentIndex < data.length && Character.isWhitespace(data[currentIndex]))
            currentIndex++;

        token = switch (state) {
            case BASIC -> lexBasicToken();
            case EXTENDED -> lexExtendedToken();
        };

        return token;
    }

    /**
     * Sets the current lexer state.
     *
     * @param state the state to change into
     */
    public void setState(LexerState state) {
        Objects.requireNonNull(state, "The lexer state must not be null.");

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
     * Gets the next from the input token using the rules for the {@link LexerState#BASIC} state
     * and returns it without storing it in {@link #token}.
     *
     * @return the token processed from the input
     * @throws LexerException if there is an error while getting the next token
     */
    private Token lexBasicToken() {
        if (token != null && token.getType() == TokenType.EOF)
            throw new LexerException("The input string has already been consumed");

        while (currentIndex < data.length && Character.isWhitespace(data[currentIndex]))
            currentIndex++;

        if (currentIndex == data.length)
            return new Token(TokenType.EOF, null);

        if (Character.isLetter(data[currentIndex]) || data[currentIndex] == '\\') {
            StringBuilder sb = new StringBuilder();

            while (currentIndex < data.length) {
                if (Character.isLetter(data[currentIndex])) {
                    sb.append(data[currentIndex++]);
                    continue;
                }

                // Characters other than letters mark the end of the word,
                // except for backslashes, which start an escape sequence instead.
                if (data[currentIndex] != '\\')
                    break;

                //Escape sequence handling:
                currentIndex++; // Skip the backslash

                if (currentIndex == data.length)
                    throw new LexerException("Invalid backslash at end of file, expected an escape sequence.");

                if (!Character.isDigit(data[currentIndex]) && data[currentIndex] != '\\')
                    throw new LexerException("Invalid escape sequence: \\" + data[currentIndex]);

                sb.append(data[currentIndex++]);
            }

            return new Token(TokenType.WORD, sb.toString());
        }

        if (Character.isDigit(data[currentIndex])) {
            StringBuilder sb = new StringBuilder();

            while (currentIndex < data.length && Character.isDigit(data[currentIndex]))
                sb.append(data[currentIndex++]);

            String numberString = sb.toString();
            long number;

            try {
                number = Long.parseLong(numberString);
            } catch (NumberFormatException e) {
                throw new LexerException("The number " + numberString + " is not representable as a Long.", e);
            }

            return new Token(TokenType.NUMBER, number);
        }

        return new Token(TokenType.SYMBOL, data[currentIndex++]);
    }

    /**
     * Gets the next from the input token using the rules for the {@link LexerState#EXTENDED} state
     * and returns it without storing it in {@link #token}.
     *
     * @return the token processed from the input
     * @throws LexerException if there is an error while getting the next token
     */
    private Token lexExtendedToken() {
        if (currentIndex == data.length)
            return new Token(TokenType.EOF, null);

        if (data[currentIndex] == '#')
            return new Token(TokenType.SYMBOL, data[currentIndex++]);

        StringBuilder sb = new StringBuilder();

        while (currentIndex < data.length && !Character.isWhitespace(data[currentIndex]) && data[currentIndex] != '#')
            sb.append(data[currentIndex++]);

        return new Token(TokenType.WORD, sb.toString());
    }
}
