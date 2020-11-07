package hr.fer.oprpp1.hw04.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A database of {@link StudentRecord}s. The records' JMBAGs are used as unique keys.
 *
 * @author Borna Cafuk
 */
public class StudentDatabase {
    /**
     * The number of fields in every row and record.
     */
    private static final int FIELD_COUNT = 4;

    /**
     * A list of the records stored in the database.
     */
    private List<StudentRecord> records;
    /**
     * An index used to quickly retrieve records by their JMBAG.
     */
    private Map<String, StudentRecord> jmbagIndex;

    /**
     * Creates a new database from an array of strings representing records in the form of tab-separated fields.
     * <p>
     * Every string in the array is expected to contain exactly {@value #FIELD_COUNT} tab-separated fields, in this order:
     * <ul>
     *     <li>JMBAG,</li>
     *     <li>last name,</li>
     *     <li>first name,</li>
     *     <li>final grade.</li>
     * </ul>
     *
     * @param rows an array of strings representing the records to be stored in the database
     * @throws IllegalArgumentException if a row does not contain exactly {@value #FIELD_COUNT} fields
     * @throws NumberFormatException    if a final grade cannot be parsed into an integer
     * @throws IllegalArgumentException if the final grade is not between {@value StudentRecord#MIN_GRADE} and {@value StudentRecord#MAX_GRADE}
     * @throws IllegalArgumentException if there are two rows with the same JMBAG
     */
    public StudentDatabase(String[] rows) {
        records = new ArrayList<>(rows.length);
        jmbagIndex = new HashMap<>(rows.length);

        for (String row : rows) {
            StudentRecord record = parseRow(row);

            if (jmbagIndex.containsKey(record.getJmbag()))
                throw new IllegalArgumentException("Duplicate JMBAG in input: " + record.getJmbag());

            records.add(record);
            jmbagIndex.put(record.getJmbag(), record);
        }
    }

    /**
     * Finds a record by its JMBAG.
     *
     * @param jmbag the JMBAG to search by
     * @return the record with the given JMBAG, or {@code null} if none was found
     */
    public StudentRecord forJMBAG(String jmbag) {
        return jmbagIndex.get(jmbag);
    }

    /**
     * Returns a list of only those records from the database which are accepted by a {@link IFilter filter}.
     *
     * @param filter the filter to use
     * @return a filtered list of entries
     */
    public List<StudentRecord> filter(IFilter filter) {
        return records.stream()
                      .filter(filter::accepts)
                      .collect(Collectors.toList());
    }

    /**
     * Parses a string of tab-separated fields into a {@link StudentRecord}.
     *
     * @param row the row to be parsed
     * @return the resulting record
     * @throws IllegalArgumentException if the row does not contain exactly {@value #FIELD_COUNT} fields
     * @throws NumberFormatException    if the final grade cannot be parsed into an integer
     * @throws IllegalArgumentException if the final grade is not between {@value StudentRecord#MIN_GRADE} and {@value StudentRecord#MAX_GRADE}
     */
    private static StudentRecord parseRow(String row) {
        // The row is expected to have exactly FIELD_COUNT fields, but
        // one more is used as the limit so that excess fields can be easily detected.
        String[] fields = row.split("\t", FIELD_COUNT + 1);

        if (fields.length < FIELD_COUNT)
            throw new IllegalArgumentException("Expected " + FIELD_COUNT + " fields, but instead got " + fields.length + " fields: " + row);
        if (fields.length > FIELD_COUNT)
            throw new IllegalArgumentException("Expected " + FIELD_COUNT + " fields, but instead got more fields: " + row);

        return new StudentRecord(fields[0], fields[1], fields[2], Integer.parseInt(fields[3]));
    }
}
