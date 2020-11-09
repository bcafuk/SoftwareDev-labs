package hr.fer.oprpp1.hw04.db;

import java.util.Objects;

/**
 * Represents a conditional expression comparing a field of a record with a constant string.
 *
 * @author Borna Cafuk
 */
public class ConditionalExpression {
    /**
     * The getter used to retrieve the value of a field.
     */
    private IFieldValueGetter fieldGetter;
    /**
     * The string literal with which to compare the field's value.
     */
    private String stringLiteral;
    /**
     * The operator to use to compare them.
     */
    private IComparisonOperator comparisonOperator;

    /**
     * Constructs a new conditional expression with the given parameters.
     *
     * @param fieldGetter        the field getter to use to get for the first operand of the expression
     * @param stringLiteral      the string literal to use as the second operand of the expression
     * @param comparisonOperator the comparison operator to use in the expression
     * @throws NullPointerException if any of {@code fieldGetter}, {@code stringLiteral}, or {@code comparisonOperator} is {@code null}
     */
    public ConditionalExpression(IFieldValueGetter fieldGetter, String stringLiteral,
                                 IComparisonOperator comparisonOperator) {
        this.fieldGetter = Objects.requireNonNull(fieldGetter, "The field getter must not be null.");
        this.stringLiteral = Objects.requireNonNull(stringLiteral, "The string literal must not be null.");
        this.comparisonOperator = Objects.requireNonNull(comparisonOperator, "The comparison operator must not be null.");
    }

    /**
     * Checks whether a given record satisfies the expression.
     *
     * @param record the record for which to check the value
     * @return {@code true} if the record satisfies the expression, {@code false} otherwise
     */
    public boolean satisfies(StudentRecord record) {
        return comparisonOperator.satisfied(fieldGetter.get(record), stringLiteral);
    }

    /**
     * Gets the field getter for the first operand of the expression.
     *
     * @return the field getter
     */
    public IFieldValueGetter getFieldGetter() {
        return fieldGetter;
    }

    /**
     * Gets the string literal used as the second operand of the expression.
     *
     * @return the string literal
     */
    public String getStringLiteral() {
        return stringLiteral;
    }

    /**
     * Gets the comparison operator of the expression.
     *
     * @return the comparison operator
     */
    public IComparisonOperator getComparisonOperator() {
        return comparisonOperator;
    }
}
