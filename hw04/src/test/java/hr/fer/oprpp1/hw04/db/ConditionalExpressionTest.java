package hr.fer.oprpp1.hw04.db;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConditionalExpressionTest {
    @Test
    void testConstructor() {
        ConditionalExpression expression = new ConditionalExpression(FieldValueGetters.JMBAG, "0000000001", ComparisonOperators.EQUALS);

        assertSame(FieldValueGetters.JMBAG, expression.getFieldGetter());
        assertEquals("0000000001", expression.getStringLiteral());
        assertSame(ComparisonOperators.EQUALS, expression.getComparisonOperator());
    }

    @Test
    void testConstructorThrowsForNull() {
        assertThrows(NullPointerException.class, () -> new ConditionalExpression(null, "0000000001", ComparisonOperators.EQUALS));
        assertThrows(NullPointerException.class, () -> new ConditionalExpression(FieldValueGetters.JMBAG, null, ComparisonOperators.EQUALS));
        assertThrows(NullPointerException.class, () -> new ConditionalExpression(FieldValueGetters.JMBAG, "0000000001", null));
    }

    @Test
    void testValueFor() {
        ConditionalExpression expression = new ConditionalExpression(FieldValueGetters.JMBAG, "0000000001", ComparisonOperators.EQUALS);

        assertTrue(expression.valueFor(new StudentRecord("0000000001", "Last", "First", 5)));
        assertFalse(expression.valueFor(new StudentRecord("0000000002", "Last", "First", 5)));
    }
}
