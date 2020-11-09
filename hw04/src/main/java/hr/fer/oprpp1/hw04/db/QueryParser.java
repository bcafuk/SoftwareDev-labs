package hr.fer.oprpp1.hw04.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Parses queries into the database.
 *
 * @author Borna Cafuk
 */
public class QueryParser {
    /**
     * A map used to look up which field name corresponds to which value getter object.
     */
    private static final Map<String, IFieldValueGetter> FIELD_GETTERS = Map.of(
            "jmbag", FieldValueGetters.JMBAG,
            "lastName", FieldValueGetters.LAST_NAME,
            "firstName", FieldValueGetters.FIRST_NAME
    );
    /**
     * A map used to look up which operator string corresponds to which operator object.
     */
    private static final Map<String, IComparisonOperator> OPERATORS = Map.of(
            "<", ComparisonOperators.LESS,
            "<=", ComparisonOperators.LESS_OR_EQUALS,
            ">", ComparisonOperators.GREATER,
            ">=", ComparisonOperators.GREATER_OR_EQUALS,
            "=", ComparisonOperators.EQUALS,
            "!=", ComparisonOperators.NOT_EQUALS,
            "LIKE", ComparisonOperators.LIKE
    );
    /**
     * The word used to link multiple conditional expressions together.
     */
    private static final String EXPRESSION_LINKER = "and";
    /**
     * The character used to delimit strings.
     */
    private static final char STRING_DELIMITER = '"';

    /**
     * The input text of the query.
     */
    private String queryText;
    /**
     * The index of the first character in the {@link #queryText} which has not yet been consumed.
     */
    private int currentPosition = 0;
    /**
     * The result of parsing.
     */
    private List<ConditionalExpression> expressions = new ArrayList<>();

    /**
     * Parses a query.
     *
     * @param query the query string to be parsed
     * @throws IllegalArgumentException if the query is syntactically invalid
     */
    public QueryParser(String query) {
        queryText = query;

        do {
            expressions.add(parseExpression());
        } while (tryConsumeLinker());
    }

    /**
     * Checks if the query compares only whether the JMBAG is equal to some string.
     * <p>
     * Such queries can be executed in constant time, so it makes sense to be able to check for them.
     *
     * @return {@code true} if the query consists of only one equality comparison of the JMBAG field,
     *         {@code false} otherwise
     */
    public boolean isDirectQuery() {
        if (expressions.size() != 1)
            return false;

        ConditionalExpression expression = expressions.get(0);
        return expression.getFieldGetter() == FieldValueGetters.JMBAG &&
                expression.getComparisonOperator() == ComparisonOperators.EQUALS;
    }

    /**
     * If the query is a {@link #isDirectQuery() direct query}, returns the string literal with which the JMBAG is
     * being compared.
     *
     * @return the string literal of the comparison
     * @throws IllegalStateException if the query is not a direct query
     */
    public String getQueriedJMBAG() {
        if (!isDirectQuery())
            throw new IllegalStateException("The query is not direct.");

        return expressions.get(0).getStringLiteral();
    }

    public List<ConditionalExpression> getQuery() {
        return List.copyOf(expressions);
    }

    /**
     * Parses a {@link ConditionalExpression}.
     *
     * @return the parsed expression
     */
    private ConditionalExpression parseExpression() {
        IFieldValueGetter fieldGetter = fieldNameLexer.lex();
        IComparisonOperator operator = operatorLexer.lex();
        String literal = stringLexer.lex();

        return new ConditionalExpression(fieldGetter, literal, operator);
    }

    /**
     * Consumes characters from the input as long as they are whitespace.
     */
    private void skipWhitespace() {
        while (currentPosition < queryText.length() && Character.isWhitespace(queryText.charAt(currentPosition)))
            currentPosition++;
    }

    /**
     * Tries to consume a linker word {@value EXPRESSION_LINKER} from the input.
     * <p>
     * Consumes all preceding whitespace.
     *
     * @return {@code true} if the next word in the input is {@value EXPRESSION_LINKER} followed by whitespace,
     *         {@code false} if the end of the input is reached
     * @throws IllegalArgumentException if the remaining input does not consist of whitespace,
     *                                  but doesn't start with {@value EXPRESSION_LINKER} followed by whitespace.
     */
    private boolean tryConsumeLinker() {
        skipWhitespace();

        if (currentPosition == queryText.length())
            return false;

        if (currentPosition + 3 >= queryText.length() ||
                !queryText.substring(currentPosition, currentPosition + EXPRESSION_LINKER.length()).toLowerCase().equals(EXPRESSION_LINKER))
            throw new IllegalArgumentException("Expected \"" + EXPRESSION_LINKER + "\" or end of input, but got \"" + queryText.substring(currentPosition) + "\"");
        currentPosition += EXPRESSION_LINKER.length(); // Skip the linker

        if (currentPosition == queryText.length() || !Character.isWhitespace(queryText.charAt(currentPosition)))
            throw new IllegalArgumentException("Expected whitespace after \"" + EXPRESSION_LINKER + "\"");
        currentPosition++; // Skip one whitespace character

        return true;
    }

    /**
     * A lexer used to lex a field name into a field value getter.
     */
    private Lexer<IFieldValueGetter> fieldNameLexer = new Lexer<>(
            Character::isLetterOrDigit,
            fieldName -> {
                if (fieldName.isEmpty())
                    throw new IllegalArgumentException("Missing field name");

                IFieldValueGetter fieldGetter = FIELD_GETTERS.get(fieldName);
                if (fieldGetter == null)
                    throw new IllegalArgumentException("Unknown field name " + fieldName);

                return fieldGetter;
            }
    );

    /**
     * A lexer used to lex operators into a comparison operator object.
     */
    private Lexer<IComparisonOperator> operatorLexer = new Lexer<>(
            c -> !Character.isWhitespace(c) && c != STRING_DELIMITER,
            operatorString -> {
                if (operatorString.isEmpty())
                    throw new IllegalArgumentException("Missing operator");

                IComparisonOperator operator = OPERATORS.get(operatorString);
                if (operator == null)
                    throw new IllegalArgumentException("Unknown operator " + operatorString);

                return operator;
            }
    );

    /**
     * A lexer used to lex string literals.
     */
    private Lexer<String> stringLexer = new Lexer<>(
            STRING_DELIMITER,
            c -> c != STRING_DELIMITER,
            STRING_DELIMITER,
            s -> s
    );

    /**
     * A generic lexer for use in the parser.
     *
     * @param <T> the return type of the {@link #lex()} method.
     */
    private class Lexer<T> {
        /**
         * The character which is required at the start of the token, or {@code null} if there is no such requirement.
         * <p>
         * The prefix is consumed from the input, but not included in the string passed to {@link #mapOutput}.
         */
        Character prefix;
        /**
         * The condition under which the lexer will continue to consume characters.
         */
        Predicate<Character> consumeCondition;
        /**
         * The character which is required at the end of the token, or {@code null} if there is no such requirement.
         * <p>
         * The suffix is consumed from the input, but not included in the string passed to {@link #mapOutput}.
         */
        Character suffix;
        /**
         * The function mapping the string consumed to a return value.
         */
        Function<String, T> mapOutput;

        /**
         * Constructs a lexer with no requirement for a prefix or a suffix.
         *
         * @param consumeCondition the condition under which the lexer will continue to consume characters
         * @param mapOutput        the function mapping the string consumed to a return value
         */
        public Lexer(Predicate<Character> consumeCondition, Function<String, T> mapOutput) {
            this(null, consumeCondition, null, mapOutput);
        }

        /**
         * Constructs a lexer with the required token prefix and suffix.
         *
         * @param prefix           the required prefix, or {@code null} if none is required
         * @param consumeCondition the condition under which the lexer will continue to consume characters
         * @param suffix           the required suffix, or {@code null} if none is required
         * @param mapOutput        the function mapping the string consumed to a return value
         */
        public Lexer(Character prefix, Predicate<Character> consumeCondition,
                     Character suffix, Function<String, T> mapOutput) {
            this.prefix = prefix;
            this.consumeCondition = consumeCondition;
            this.suffix = suffix;
            this.mapOutput = mapOutput;
        }

        /**
         * Consumes characters from the input and maps them to an output.
         * <p>
         * Calls {@link #skipWhitespace()} first.
         * Then, the {@link #prefix} is checked.
         * Next, characters are consumed as long as {@link #consumeCondition} returns {@code true} for them
         * Finally, the {@link #suffix} is checked.
         *
         * @return the result of {@link #mapOutput} for the consumed characters
         * @throws IllegalArgumentException if there is no more input after skipping whitespace
         * @throws IllegalArgumentException if a prefix or suffix is required, but is not present
         */
        T lex() {
            skipWhitespace();

            if (currentPosition == queryText.length())
                throw new IllegalArgumentException("Unexpectedly got to the end of the query.");

            if (prefix != null) {
                if (queryText.charAt(currentPosition) != prefix)
                    throw new IllegalArgumentException("Expected " + prefix + ", but got " + queryText.charAt(currentPosition));
                currentPosition++; // Skip the prefix
            }

            int start = currentPosition;
            while (currentPosition < queryText.length() && consumeCondition.test(queryText.charAt(currentPosition)))
                currentPosition++;
            String token = queryText.substring(start, currentPosition);

            if (suffix != null) {
                if (currentPosition == queryText.length())
                    throw new IllegalArgumentException("Expected " + suffix + ", but got to end of the query.");
                if (queryText.charAt(currentPosition) != suffix)
                    throw new IllegalArgumentException("Expected " + suffix + ", but got " + queryText.charAt(currentPosition));
                currentPosition++; // Skip the suffix
            }

            return mapOutput.apply(token);
        }
    }
}
