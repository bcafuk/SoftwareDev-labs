package hr.fer.oprpp1.custom.scripting.lexer;

/**
 * Represents an exception that occurred when a {@link SmartScriptLexer} was performing one of its operations.
 *
 * @author Borna Cafuk
 */
public class SmartScriptLexerException extends RuntimeException {
    /**
     * Constructs a new lexer exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public SmartScriptLexerException(String message) {
        super(message);
    }

    /**
     * Constructs a new lexer exception with the specified detail message and
     * cause. <p>Note that the detail message associated with
     * {@code cause} is <i>not</i> automatically incorporated in
     * this runtime exception's detail message.
     *
     * @param message the detail message (which is saved for later retrieval
     *                by the {@link #getMessage()} method).
     * @param cause   the cause (which is saved for later retrieval by the
     *                {@link #getCause()} method).  (A {@code null} value is
     *                permitted, and indicates that the cause is nonexistent or
     *                unknown.)
     */
    public SmartScriptLexerException(String message, Throwable cause) {
        super(message, cause);
    }
}
