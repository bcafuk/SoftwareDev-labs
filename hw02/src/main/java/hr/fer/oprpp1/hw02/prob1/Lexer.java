package hr.fer.oprpp1.hw02.prob1;

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
     * Creates a lexer for a given string.
     *
     * @param text the text which will be tokenized by this lexer
     */
    public Lexer(String text) {
        data = text.toCharArray();
    }

    /**
     * Gets the next from the input token and returns it.
     *
     * @return the token processed from the input
     * @throws LexerException if there is an error while getting the next token
     */
    public Token nextToken() {
        // TODO: Implement method
        return null;
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
}
