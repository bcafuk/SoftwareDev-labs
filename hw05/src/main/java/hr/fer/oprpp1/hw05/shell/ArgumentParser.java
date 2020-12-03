package hr.fer.oprpp1.hw05.shell;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * A class used to parse argument strings.
 *
 * @author Borna Cafuk
 */
public class ArgumentParser {
    /**
     * The argument string to parse.
     */
    private String argumentString;
    /**
     * The index of the first character which has not yet been parsed.
     */
    private int parsingPosition;

    /**
     * Constructs a parser for a given argument string.
     *
     * @param argumentString the string to parse
     */
    public ArgumentParser(String argumentString) {
        this.argumentString = argumentString;

        skipWhitespace();
    }

    /**
     * Checks whether there are any more arguments in the string.
     *
     * @return {@code true} if there is at least one more argument left to parse, {@code false} otherwise
     */
    public boolean hasNextArgument() {
        return parsingPosition != argumentString.length();
    }

    /**
     * Parses the next argument from the input string.
     *
     * @return the next argument
     * @throws NoSuchElementException   if there are no more arguments
     *                                  (i.e. {@link #hasNextArgument()} would have returned {@code false})
     * @throws IllegalArgumentException if the format of a quoted argument is invalid
     */
    public String nextArgument() {
        if (!hasNextArgument())
            throw new NoSuchElementException("No more arguments are present.");

        String argument;
        if (argumentString.charAt(parsingPosition) == '"')
            argument = parseQuoted();
        else
            argument = parseUnquoted();

        skipWhitespace();
        return argument;
    }

    /**
     * Parses an unquoted argument.
     *
     * @return the argument
     */
    private String parseUnquoted() {
        int beginIndex = parsingPosition;

        while (parsingPosition != argumentString.length() && !Character.isWhitespace(argumentString.charAt(parsingPosition)))
            parsingPosition++;

        return argumentString.substring(beginIndex, parsingPosition);
    }

    /**
     * Parses a quoted argument.
     *
     * @return the argument
     * @throws IllegalArgumentException if the argument is not terminated using a closing qutation mark
     * @throws IllegalArgumentException if there is no whitespace or end of input after the closing quote
     */
    private String parseQuoted() {
        parsingPosition++; // Skip the opening quotation mark
        if (parsingPosition == argumentString.length())
            throw new IllegalArgumentException("Expected additional input after opening quote.");

        StringBuilder sb = new StringBuilder();

        while (argumentString.charAt(parsingPosition) != '"') {
            if (argumentString.charAt(parsingPosition) == '\\') {
                parsingPosition++; // Skip the backslash

                if (parsingPosition == argumentString.length())
                    throw new IllegalArgumentException("Unterminated quoted argument.");

                switch (argumentString.charAt(parsingPosition)) {
                    case '\\', '\"' -> sb.append(argumentString.charAt(parsingPosition));
                    default -> sb.append('\\').append(argumentString.charAt(parsingPosition));
                }

            } else {
                sb.append(argumentString.charAt(parsingPosition));
            }

            parsingPosition++;
            if (parsingPosition == argumentString.length())
                throw new IllegalArgumentException("Unterminated quoted argument.");
        }

        parsingPosition++; // Skip the closing quotation mark

        if (parsingPosition != argumentString.length() && !Character.isWhitespace(argumentString.charAt(parsingPosition)))
            throw new IllegalArgumentException("Expected whitespace or end of input after closing quote.");

        return sb.toString();
    }

    /**
     * Skip consecutive whitespace.
     */
    private void skipWhitespace() {
        while (parsingPosition != argumentString.length() && Character.isWhitespace(argumentString.charAt(parsingPosition)))
            parsingPosition++;
    }

    /**
     * Parses all arguments and collects them into a list.
     *
     * @param argumentString the string to parse
     * @return a list containing all arguments
     */
    public static List<String> parseAll(String argumentString) {
        ArgumentParser parser = new ArgumentParser(argumentString);

        List<String> arguments = new ArrayList<>();
        while (parser.hasNextArgument())
            arguments.add(parser.nextArgument());

        return arguments;
    }
}
