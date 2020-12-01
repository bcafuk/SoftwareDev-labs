package hr.fer.oprpp1.hw05.crypto;

/**
 * A collection of static utility methods used to convert between hexadecimal strings and arrays of bytes.
 *
 * @author Borna Cafuk
 */
public final class Util {
    /**
     * Don't let anyone instantiate this class.
     */
    private Util() {}

    /**
     * Decodes a hexadecimal string into an array of bytes.
     * <p>
     * The input string can be empty, in which case an array of zero length is returned.
     * Two's complement is used for negative values.
     * <p>
     * The following characters are allowed as hexadecimal digits:
     * <blockquote>{@code 0123456789abcdefABCDEF}</blockquote>
     * <p>
     * These are the characters {@code '\u005Cu0030'} through {@code '\u005Cu0039'},
     * {@code '\u005Cu0061'} through {@code '\u005Cu0066'}, and {@code '\u005Cu0041'} through {@code '\u005Cu0046'}.
     * <p>
     * That is, for the digits {@code a} through {@code f}, both lowercase and uppercase are supported.
     *
     * @param keyText a hex string to decode
     * @return the corresponding array of bytes
     * @throws NullPointerException     if {@code keyText} is {@code null}
     * @throws IllegalArgumentException if {@code keyText}'s length is odd
     * @throws IllegalArgumentException if {@code keyText} contains any characters other than hexadecimal digits
     */
    public static byte[] hextobyte(String keyText) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    /**
     * Encodes an array of bytes into a hexadecimal string.
     * <p>
     * The input array can be of length 0, in which case an empty string is returned.
     * Two's complement is used for negative values.
     * <p>
     * The following characters are used as hexadecimal digits:
     * <blockquote>{@code 0123456789abcdef}</blockquote>
     * <p>
     * These are the characters {@code '\u005Cu0030'} through {@code '\u005Cu0039'}, and
     * {@code '\u005Cu0061'} through {@code '\u005Cu0066'}.
     *
     * @param bytearray the array to encode
     * @return the resulting hexadecimal string
     * @throws NullPointerException if {@code bytearray} is {@code null}
     */
    public static String bytetohex(byte[] bytearray) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }
}
