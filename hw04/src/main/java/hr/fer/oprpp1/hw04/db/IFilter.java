package hr.fer.oprpp1.hw04.db;

/**
 * A functional interface used to filter {@link StudentRecord}s.
 *
 * @author Borna Cafuk
 * @see java.util.function.Predicate
 */
@FunctionalInterface
public interface IFilter {
    /**
     * Checks whether the filter accepts a given record.
     *
     * @param record the record to check
     * @return {@code true} if the filter accepts {@code record}, {@code false} if it does not
     */
    boolean accepts(StudentRecord record);
}
