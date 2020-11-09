package hr.fer.oprpp1.hw04.db;

import java.util.Objects;

/**
 * A database record representing a single student.
 *
 * @author Borna Cafuk
 */
public class StudentRecord {
    /**
     * The lowest possible final grade.
     */
    public static final int MIN_GRADE = 1;
    /**
     * The highest possible final grade.
     */
    public static final int MAX_GRADE = 5;

    /**
     * The student's unique 10-digit identifier used in the Republic of Croatia.
     * <p>
     * JMBAG stands for jedinstveni matični broj akademskog građanina (unique master academic citizen number).
     */
    private String jmbag;
    /**
     * The student's last name.
     */
    private String lastName;
    /**
     * The student's first name.
     */
    private String firstName;
    /**
     * The student's final grade, between 1 and 5.
     */
    private int finalGrade;

    /**
     * Constructs a new student record with the given parameters.
     *
     * @param jmbag      the student's unique 10-digit identifier
     * @param lastName   the student's last name
     * @param firstName  the student's first name
     * @param finalGrade the student's final grade
     * @throws NullPointerException     if any of {@code jmbag}, {@code firstName}, or {@code finalGrade} is {@code null}
     * @throws IllegalArgumentException if the final grade is not between {@value MIN_GRADE} and {@value MAX_GRADE}
     */
    public StudentRecord(String jmbag, String lastName, String firstName, int finalGrade) {
        if (finalGrade < MIN_GRADE || finalGrade > MAX_GRADE)
            throw new IllegalArgumentException("Final grade was expected to be between " + MIN_GRADE + " and " + MAX_GRADE + ", but was " + finalGrade);

        this.jmbag = Objects.requireNonNull(jmbag, "jmbag must not be null.");
        this.lastName = Objects.requireNonNull(lastName, "lastName must not be null.");
        this.firstName = Objects.requireNonNull(firstName, "firstName must not be null.");
        this.finalGrade = finalGrade;
    }

    /**
     * Retrieves the student's unique identifier.
     *
     * @return the student's unique identifier
     */
    public String getJmbag() {
        return jmbag;
    }

    /**
     * Retrieves the student's last name.
     *
     * @return the student's last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Retrieves the student's first name.
     *
     * @return the student's first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Retrieves the student's final grade.
     *
     * @return the student's final grade
     */
    public int getFinalGrade() {
        return finalGrade;
    }

    /**
     * Compares two student records by their {@link #jmbag} fields.
     *
     * @param o the reference object with which to compare
     * @return {@code true} if {@code o} is a student record with the same JMBAG, {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof StudentRecord))
            return false;

        StudentRecord that = (StudentRecord) o;
        return jmbag.equals(that.jmbag);
    }

    /**
     * Returns the hash code for the {@link #jmbag} field.
     *
     * @return a hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(jmbag);
    }
}
