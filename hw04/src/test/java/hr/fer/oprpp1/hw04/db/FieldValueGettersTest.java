package hr.fer.oprpp1.hw04.db;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FieldValueGettersTest {
    @Test
    public void testJmbagValueGetter() {
        StudentRecord record = new StudentRecord("0000000001", "Last", "First", 5);
        assertEquals("0000000001", FieldValueGetters.JMBAG.get(record));
    }

    @Test
    public void testLastNameValueGetter() {
        StudentRecord record = new StudentRecord("0000000001", "Last", "First", 5);
        assertEquals("Last", FieldValueGetters.LAST_NAME.get(record));
    }
    @Test
    public void testFirstNameValueGetter() {
        StudentRecord record = new StudentRecord("0000000001", "Last", "First", 5);
        assertEquals("First", FieldValueGetters.FIRST_NAME.get(record));
    }
}
