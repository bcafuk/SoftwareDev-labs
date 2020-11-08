package hr.fer.oprpp1.hw04.db;

/**
 * A functional interface used to get values of fields of {@link StudentRecord}s.
 *
 * @author Borna Cafuk
 * @see java.util.function.Function
 */
@FunctionalInterface
public interface IFieldValueGetter {
    /**
     * Gets the value of a field of a record.
     *
     * @param record the record whose field to get
     * @return the value of the field
     */
    String get(StudentRecord record);
}
