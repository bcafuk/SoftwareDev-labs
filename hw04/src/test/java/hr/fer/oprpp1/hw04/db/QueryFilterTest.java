package hr.fer.oprpp1.hw04.db;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class QueryFilterTest {
    @Test
    public void testConstructorThrowsForNull() {
        assertThrows(NullPointerException.class, () -> new QueryFilter(null));
        assertThrows(NullPointerException.class, () -> new QueryFilter(List.of(null, null)));
        assertThrows(NullPointerException.class, () -> new QueryFilter(List.of(
                new ConditionalExpression(FieldValueGetters.JMBAG, "0000000001", ComparisonOperators.EQUALS),
                new ConditionalExpression(FieldValueGetters.LAST_NAME, "J", ComparisonOperators.GREATER),
                null,
                new ConditionalExpression(FieldValueGetters.FIRST_NAME, "*a", ComparisonOperators.LIKE)
        )));
    }

    @Test
    public void testAccepts() {
        QueryFilter filter = new QueryFilter(List.of(
                new ConditionalExpression(FieldValueGetters.JMBAG, "0000000001", ComparisonOperators.EQUALS),
                new ConditionalExpression(FieldValueGetters.LAST_NAME, "J", ComparisonOperators.GREATER),
                new ConditionalExpression(FieldValueGetters.FIRST_NAME, "*a", ComparisonOperators.LIKE)
        ));

        assertFalse(filter.accepts(new StudentRecord("0000000016", "Glumac", "Milan", 5)));
        assertFalse(filter.accepts(new StudentRecord("0000000016", "Glumac", "Jelena", 5)));
        assertFalse(filter.accepts(new StudentRecord("0000000016", "Martinec", "Milan", 5)));
        assertFalse(filter.accepts(new StudentRecord("0000000016", "Martinec", "Jelena", 5)));
        assertFalse(filter.accepts(new StudentRecord("0000000001", "Glumac", "Milan", 5)));
        assertFalse(filter.accepts(new StudentRecord("0000000001", "Glumac", "Jelena", 5)));
        assertFalse(filter.accepts(new StudentRecord("0000000001", "Martinec", "Milan", 5)));
        assertTrue(filter.accepts(new StudentRecord("0000000001", "Martinec", "Jelena", 5)));
    }
}
