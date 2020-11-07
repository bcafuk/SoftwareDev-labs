package hr.fer.oprpp1.hw04.db;

/**
 * A functional interface used to filter {@link StudentRecord}s.
 *
 * @author Borna Cafuk
 * @see java.util.function.Predicate
 */
@FunctionalInterface
public interface IFilter {
    boolean accepts(StudentRecord record);
}
