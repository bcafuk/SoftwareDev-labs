package hr.fer.oprpp1.hw04.db;

import java.util.List;
import java.util.Objects;

/**
 * Accepts records if and only if they satisfy all expressions in a query.
 *
 * @author Borna Cafuk
 */
public class QueryFilter implements IFilter {
    /**
     * The list of conditions to check for a record.
     */
    private List<ConditionalExpression> query;

    /**
     * Constructs a new filter for a given query.
     *
     * @param query the query to use to filter records
     * @throws NullPointerException if {@code query} or any of its elements is {@code null}
     */
    public QueryFilter(List<ConditionalExpression> query) {
        this.query = Objects.requireNonNull(query, "The query must not be null.");
        for (ConditionalExpression expression : query)
            Objects.requireNonNull(expression, "All of the expressions must not be null.");
    }

    /**
     * Checks whether the record satisfies all the conditional expressions in the query.
     *
     * @param record the record to check
     * @return {@code true} if the filter accepts {@code record}, {@code false} if it does not
     */
    @Override
    public boolean accepts(StudentRecord record) {
        for (ConditionalExpression expression : query)
            if (!expression.satisfies(record))
                return false;
        return true;
    }
}
