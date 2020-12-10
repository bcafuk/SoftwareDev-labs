package hr.fer.zemris.java.fractals.util;

import hr.fer.zemris.math.Complex;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A class used to read the roots of a polynomial from the user.
 *
 * @author Borna Cafuk
 */
public final class RootInput {
    /**
     * A piece of a regular expression to be used in {@link #REAL_PATTERN}, {@link #IMAGINARY_PATTERN},
     * and {@link #COMPLEX_PATTERN}.
     */
    static private final String NUMBER_REGEX = "\\d+(?:\\.\\d+)?";
    /**
     * A regex used in the {@link #parseComplex(String)} method to parse purely real numbers.
     */
    static private final Pattern REAL_PATTERN = Pattern.compile("[+-]?" + NUMBER_REGEX);
    /**
     * A regex used in the {@link #parseComplex(String)} method to parse purely imaginary numbers.
     */
    static private final Pattern IMAGINARY_PATTERN = Pattern.compile("([+-]?)i((?:" + NUMBER_REGEX + ")?)");
    /**
     * A regex used in the {@link #parseComplex(String)} method to parse complex numbers with both their real and
     * imaginary parts specified.
     */
    static private final Pattern COMPLEX_PATTERN = Pattern.compile(
            "([+-]?" + NUMBER_REGEX + ") ?([+-]) ?i((?:" + NUMBER_REGEX + ")?)");

    /**
     * Don't let anyone instantiate this class.
     */
    private RootInput() {}

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

        String reString = "0";
        String opString = "+";
        String imString = "0";

        Matcher realMatcher = REAL_PATTERN.matcher(s);
        Matcher imaginaryMatcher = IMAGINARY_PATTERN.matcher(s);
        Matcher complexMatcher = COMPLEX_PATTERN.matcher(s);

        if (realMatcher.matches()) {
            reString = s;
        } else if (imaginaryMatcher.matches()) {
            opString = imaginaryMatcher.group(1);
            imString = imaginaryMatcher.group(2);
        } else if (complexMatcher.matches()) {
            reString = complexMatcher.group(1);
            opString = complexMatcher.group(2);
            imString = complexMatcher.group(3);
        } else {
            throw new NumberFormatException();
        }

        double re = Double.parseDouble(reString);
        double imSign = switch (opString) {
            case "+", "" -> 1.0;
            case "-" -> -1.0;
            default -> throw new NumberFormatException();
        };
        double im = imString.isEmpty() ? 1 : Double.parseDouble(imString);

        return new Complex(re, imSign * im);
    }
}
