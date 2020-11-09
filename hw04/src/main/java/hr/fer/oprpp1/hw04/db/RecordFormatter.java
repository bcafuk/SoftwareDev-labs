package hr.fer.oprpp1.hw04.db;

import java.util.ArrayList;
import java.util.List;

/**
 * Formats database records.
 *
 * @author Borna Cafuk
 */
public class RecordFormatter {
    /**
     * Disallow instantiation except by subclasses.
     */
    protected RecordFormatter() {}

    /**
     * Formats a list of records.
     *
     * @param records the records to format
     * @return a list of lines
     */
    public static List<String> format(List<StudentRecord> records) {
        int maxJmbagLength = 0;
        int maxLastNameLength = 0;
        int maxFirstNameLength = 0;
        for (StudentRecord record : records) {
            maxJmbagLength = Math.max(record.getJmbag().length(), maxJmbagLength);
            maxLastNameLength = Math.max(record.getLastName().length(), maxLastNameLength);
            maxFirstNameLength = Math.max(record.getFirstName().length(), maxFirstNameLength);
        }

        String headerFooter = '+' +
                "=".repeat(maxJmbagLength + 2) +
                '+' +
                "=".repeat(maxLastNameLength + 2) +
                '+' +
                "=".repeat(maxFirstNameLength + 2) +
                '+' +
                "=".repeat(3) +
                '+';

        List<String> lines = new ArrayList<>();
        lines.add(headerFooter);

        for (StudentRecord record : records)
            lines.add("| " + padString(record.getJmbag(), maxJmbagLength) +
                    " | " + padString(record.getLastName(), maxLastNameLength) +
                    " | " + padString(record.getFirstName(), maxFirstNameLength) +
                    " | " + record.getFinalGrade() + " |");

        lines.add(headerFooter);

        return lines;
    }

    /**
     * Adds spaces to the end of a string.
     * <p>
     * The input isn't changed if it is already of the specified length or longer.
     *
     * @param s      the input string
     * @param length the length to which to pad
     * @return the padded string.
     */
    private static String padString(String s, int length) {
        StringBuilder sb = new StringBuilder(s);

        while (sb.length() < length)
            sb.append(' ');

        return sb.toString();
    }
}
