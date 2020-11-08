package hr.fer.oprpp1.hw04.db;

/**
 * Implements field value getters used in queries.
 *
 * @author Borna Cafuk
 * @see IFieldValueGetter
 */
public final class FieldValueGetters {
    /**
     * Prevent instantiation.
     */
    private FieldValueGetters() {}

    /**
     * Gets the value of a record's field containing the student's first name.
     */
    public static final IFieldValueGetter FIRST_NAME = StudentRecord::getFirstName;

    /**
     * Gets the value of a record's field containing the student's last name.
     */
    public static final IFieldValueGetter LAST_NAME = StudentRecord::getLastName;

    /**
     * Gets the value of a record's field containing the student's unique identifier.
     */
    public static final IFieldValueGetter JMBAG = StudentRecord::getJmbag;
}
