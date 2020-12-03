package hr.fer.oprpp1.hw05.shell;

/**
 * The exception thrown when an error occurs in {@link Environment}'s input and output methods.
 *
 * @author Borna Cafuk
 */
public class ShellIOException extends RuntimeException {
    public ShellIOException() {}

    public ShellIOException(String message) {
        super(message);
    }

    public ShellIOException(String message, Throwable cause) {
        super(message, cause);
    }

    public ShellIOException(Throwable cause) {
        super(cause);
    }
}
