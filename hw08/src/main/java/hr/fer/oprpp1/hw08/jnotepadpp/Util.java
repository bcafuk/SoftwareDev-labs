package hr.fer.oprpp1.hw08.jnotepadpp;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Locale;
import java.util.Objects;

/**
 * Utility functions for JNotepad++.
 *
 * @author Borna Cafuk
 */
public final class Util {
    /**
     * Don't let anyone instantiate this class.
     */
    private Util() {}

    /**
     * Loads an icon from a resource.
     *
     * @param path the path to the resource
     * @return the loaded icon
     * @throws NullPointerException     if {@code path} is {@code null}
     * @throws IllegalArgumentException if no resource with the path is found
     * @throws UncheckedIOException     if an IO exception occurs while reading the icon from disk
     */
    public static ImageIcon loadIcon(String path) {
        Objects.requireNonNull(path, "The path must not be null");

        try (InputStream is = Util.class.getResourceAsStream(path)) {
            if (is == null)
                throw new IllegalArgumentException("Missing icon resource: " + path);
            byte[] bytes = is.readAllBytes();

            return new ImageIcon(bytes);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Inverts the letter case in a string.
     * <p>
     * This method respects the locale partially. As the conversion is done one code point at a time,
     * some characters with combining marks are not converted in the same way as by
     * {@link String#toLowerCase(Locale)} and {@link String#toUpperCase(Locale)}.
     *
     * @param string the string to transform
     * @param locale the locale to use when inverting the case
     * @return {@code string} with the letter case inverted, or {@code null} if {@code string} is {@code null}
     * @throws NullPointerException if {@code locale} is {@code null}
     */
    public static String swapCase(String string, Locale locale) {
        Objects.requireNonNull(locale, "The locale must not be null");

        if (string == null)
            return null;

        return string.codePoints()
                     .mapToObj(cp -> {
                         String c = new String(new int[]{cp}, 0, 1);

                         if (Character.isLowerCase(cp))
                             return c.toUpperCase(locale);
                         else if (Character.isUpperCase(cp) || Character.isTitleCase(cp))
                             return c.toLowerCase(locale);
                         else
                             return c;
                     })
                     .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                     .toString();
    }
}
