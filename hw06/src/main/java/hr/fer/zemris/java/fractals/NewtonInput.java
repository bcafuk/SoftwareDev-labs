package hr.fer.zemris.java.fractals;

import hr.fer.zemris.math.Complex;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.*;

/**
 * A class used to read polynomial definitions from the user.
 *
 * @author Borna Cafuk
 */
public final class NewtonInput {
    /**
     * Don't let anyone instantiate this class.
     */
    private NewtonInput() {}

    /**
     * Interactively read a list of roots from a stream.
     *
     * @param inputStream  the stream from which to read
     * @param outputStream the stream to which to write the interactive prompts
     * @return a list of the roots entered
     * @throws NullPointerException  if {@code inputStream} or {@code outputStream} is {@code null}
     * @throws IllegalStateException if the input stream ends prematurely
     */
    public static List<Complex> readPolynomial(InputStream inputStream, PrintStream outputStream) {
        Objects.requireNonNull(inputStream, "The input stream must not be null");
        Objects.requireNonNull(outputStream, "The output stream must not be null");

        Scanner inputScanner = new Scanner(inputStream);

        ArrayList<Complex> roots = new ArrayList<>();

        outputStream.println("Please enter at least two roots, one root per line. Enter 'done' when done.");

        while (true) {
            outputStream.format("Root %d> ", roots.size() + 1);

            String line;
            try {
                line = inputScanner.nextLine();
            } catch (NoSuchElementException e) {
                throw new IllegalStateException("The input stream has ended", e);
            }

            if (line.equals("done")) {
                if (roots.size() >= 2) {
                    break;
                } else {
                    outputStream.println("Please enter at least two roots.");
                    continue;
                }
            }

            try {
                roots.add(parseComplex(line));
            } catch (NumberFormatException e) {
                outputStream.println("Invalid number format.");
            }
        }

        return roots;
    }

    /**
     * Parses a string into a complex number.
     *
     * @param s the string to parse
     * @return the resulting complex number
     * @throws NullPointerException  if {@code s} is {@code null}
     * @throws NumberFormatException if the number format is invalid
     */
    public static Complex parseComplex(String s) {
        Objects.requireNonNull(s, "Cannot parse null string");

        throw new UnsupportedOperationException("Not yet implemented");
    }
}
