package hr.fer.oprpp1.hw05.crypto;

/**
 * An exception used by the {@link Crypto} class.
 *
 * @author Borna Cafuk
 */
public class CryptoException extends RuntimeException {
    public CryptoException() {}

    public CryptoException(String message) {
        super(message);
    }

    public CryptoException(String message, Throwable cause) {
        super(message, cause);
    }

    public CryptoException(Throwable cause) {
        super(cause);
    }
}
