package hr.fer.oprpp1.hw05.crypto;

import java.util.Objects;

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
        Objects.requireNonNull(keyText, "The hexadecimal string must not be null.");

        if (keyText.length() % 2 == 1)
            throw new IllegalArgumentException("The length of the key text must be even, but was " + keyText.length() + ".");

        byte[] byteArray = new byte[keyText.length() / 2];

        for (int i = 0; i < keyText.length(); i += 2) {
            int value = digitToValue(keyText.charAt(i)) * 16 + digitToValue(keyText.charAt(i + 1));
            byteArray[i / 2] = (byte) ((value < 128) ? value : value - 256);
        }

        return byteArray;
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
        Objects.requireNonNull(bytearray, "The byte array must not be null.");

        StringBuilder sb = new StringBuilder();

        for (byte b : bytearray) {
            int value = (b >= 0) ? b : b + 256;
            sb.append(valueToDigit(value / 16))
              .append(valueToDigit(value % 16));
        }

        return sb.toString();
    }

    /**
     * Converts an integer into a lowercase hexadecimal digit character.
     *
     * @param value the value to convert
     * @return the resulting hexadecimal digit
     * @throws IllegalArgumentException if {@code value} is less than 0 or greater than 15
     */
    private static char valueToDigit(int value) {
        if (value >= 0 && value <= 9)
            return (char) (value + '0');

        if (value >= 10 && value <= 15)
            return (char) (value + 'a' - 10);

        throw new IllegalArgumentException("Only numbers 0 through 15 can be converted, but " + value + " was passed.");
    }

    /**
     * Converts a hexadecimal digit into its numeric value.
     *
     * @param digit the character to convert
     * @return the character's value in hexadecimal
     * @throws IllegalArgumentException if {@code digit} is not a valid hexadecimal digit
     */
    private static int digitToValue(char digit) {
        if (digit >= '0' && digit <= '9')
            return digit - '0';

        if (digit >= 'A' && digit <= 'F')
            return digit - 'A' + 10;

        if (digit >= 'a' && digit <= 'f')
            return digit - 'a' + 10;

        throw new IllegalArgumentException("Invalid hexadecimal character '" + digit + "'.");
    }
}
