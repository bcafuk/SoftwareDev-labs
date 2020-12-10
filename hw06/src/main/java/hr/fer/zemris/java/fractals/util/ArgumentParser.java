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
     * All registered argument name strings and their keys.
     */
    private final Map<String, T> types = new HashMap<>();

    /**
     * Registers a new argument type.
     *
     * @param name the argument name
     * @param key  the type key
     * @throws NullPointerException     if {@code name} is {@code null}
     * @throws IllegalArgumentException if {@code name} has already been registered
     */
    public void registerType(String name, T key) {
        Objects.requireNonNull(name, "The argument name must not be null");

        if (types.containsKey(name))
            throw new IllegalArgumentException("Duplicate name " + name);

        types.put(name, key);
    }

    /**
     * Parses an argument array.
     * <p>
     * The elements of the array should follow this pattern:
     * argument names at even indices, followed by a string parsable to an integer at the next (odd) index.
     *
     * @param args the array of arguments and their values
     * @return a map of type keys and their values
     * @throws NullPointerException     if {@code args} or any of its elements is {@code null};
     * @throws IllegalArgumentException if {@code args} is of odd length
     * @throws IllegalArgumentException if {@code args} contains an argument name which wasn't registered
     * @throws IllegalArgumentException if {@code args} contains two names corresponding to the same type
     * @throws IllegalArgumentException if {@code args} contains a value not parsable to an integer
     */
    public Map<T, Integer> parseArguments(String[] args) {
        Objects.requireNonNull(args, "The args array must not be null");

        Map<T, Integer> parsedArguments = new HashMap<>();

        for (int i = 0; i < args.length; i += 2) {
            String name = Objects.requireNonNull(args[i], "The argument name must not be null");

            if (args.length <= i + 1)
                throw new IllegalArgumentException("Missing argument value for " + name);

            T key = types.get(name);

            if (key == null)
                throw new IllegalArgumentException("Unknown argument type " + name);
            if (parsedArguments.containsKey(key))
                throw new IllegalArgumentException("Duplicate argument for key " + key.toString());

            String valueString = Objects.requireNonNull(args[i + 1], "The argument value must not be null");
            int value;

            try {
                value = Integer.parseInt(valueString);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("The value of " + name + " (" + valueString +
                        ") cannot be parsed as an integer", e);
            }

            parsedArguments.put(key, value);
        }

        return parsedArguments;
    }
}
