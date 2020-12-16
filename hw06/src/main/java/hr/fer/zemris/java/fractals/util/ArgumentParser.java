package hr.fer.zemris.java.fractals.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A parser for integer-valued arguments.
 *
 * @param <T> the type used to identify different argument types
 */
public class ArgumentParser<T> {
    /**
     * Registered argument name strings and their keys for the form {@code --name=value}.
     */
    private final Map<String, T> longTypes = new HashMap<>();
    /**
     * Registered argument name strings and their keys for the form {@code -name value}.
     */
    private final Map<String, T> shortTypes = new HashMap<>();

    /**
     * Registers a new argument type.
     *
     * @param longName  the argument name for the long format ({@code --name=value}),
     *                  or {@code null} if no long name should be registered
     * @param shortName the argument name for the short format ({@code -name value}),
     *                  or {@code null} if no short name should be registered
     * @param key       the type key
     * @throws IllegalArgumentException if {@code longName} or {@code shortName} has already been registered
     */
    public void registerType(String longName, String shortName, T key) {
        if (longTypes.containsKey(longName))
            throw new IllegalArgumentException("Duplicate name " + longName);
        if (shortTypes.containsKey(shortName))
            throw new IllegalArgumentException("Duplicate name " + shortName);

        if (longName != null)
            longTypes.put(longName, key);
        if (shortName != null)
            shortTypes.put(shortName, key);
    }

    /**
     * Parses an argument array.
     *
     * @param args the array of arguments and their values
     * @return a map of type keys and their values
     * @throws NullPointerException     if {@code args} or any of its elements is {@code null};
     * @throws IllegalArgumentException if {@code args} contains an argument without a value
     * @throws IllegalArgumentException if {@code args} contains an argument name which wasn't registered
     * @throws IllegalArgumentException if {@code args} contains two names corresponding to the same type
     * @throws IllegalArgumentException if {@code args} contains a value not parsable to an integer
     */
    public Map<T, Integer> parseArguments(String[] args) {
        Objects.requireNonNull(args, "The args array must not be null");

        Map<T, Integer> parsedArguments = new HashMap<>();

        for (int i = 0; i < args.length; i++) {
            String argument = Objects.requireNonNull(args[i], "The argument must not be null");

            T key;
            String valueString;

            if (argument.startsWith("--")) {
                int equalsIndex = argument.indexOf('=');
                if (equalsIndex == -1)
                    throw new IllegalArgumentException("Missing = in " + argument);

                String name = argument.substring(2, equalsIndex);
                key = longTypes.get(name);
                valueString = argument.substring(equalsIndex + 1);
            } else if (argument.startsWith("-")) {
                if (args.length < i + 1)
                    throw new IllegalArgumentException("Missing value for " + argument);
                i++;

                String name = argument.substring(1);
                key = shortTypes.get(name);
                valueString = Objects.requireNonNull(args[i], "The argument value must not be null.");
            } else {
                throw new IllegalArgumentException("Unexpected " + argument);
            }

            if (key == null)
                throw new IllegalArgumentException("Unknown argument type in " + argument);
            if (parsedArguments.containsKey(key))
                throw new IllegalArgumentException("Duplicate argument for key " + key.toString());

            try {
                parsedArguments.put(key, Integer.parseInt(valueString));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("The value " + valueString + " cannot be parsed as an integer", e);
            }
        }

        return parsedArguments;
    }
}
