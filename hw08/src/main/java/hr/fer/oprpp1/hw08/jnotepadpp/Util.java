package hr.fer.oprpp1.hw08.jnotepadpp;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
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
}
