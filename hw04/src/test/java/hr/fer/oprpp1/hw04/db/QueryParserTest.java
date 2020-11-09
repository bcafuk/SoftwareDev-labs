package hr.fer.oprpp1.hw04.db;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class QueryParserTest {
    @Test
    public void testDirectQuery() {
        QueryParser qp = new QueryParser(" jmbag =\t\t\"0123456789\" ");
        assertTrue(qp.isDirectQuery());
        assertEquals("0123456789", qp.getQueriedJMBAG());
        assertEquals(1, qp.getQuery().size());

        ConditionalExpression exp = qp.getQuery().get(0);
        assertSame(FieldValueGetters.JMBAG, exp.getFieldGetter());
        assertEquals("0123456789", exp.getStringLiteral());
        assertSame(ComparisonOperators.EQUALS, exp.getComparisonOperator());
    }

    @Test
    public void testCompositeQuery() {
        QueryParser qp = new QueryParser("jmbag=\"0123456789\"and lastName>\"J\"and firstName LIKE\"*a\"");
        assertFalse(qp.isDirectQuery());
        assertThrows(IllegalStateException.class, qp::getQueriedJMBAG);
        assertEquals(3, qp.getQuery().size());

        List<ConditionalExpression> exps = qp.getQuery();
        assertExpressionsEqual(new ConditionalExpression(FieldValueGetters.JMBAG, "0123456789", ComparisonOperators.EQUALS), exps.get(0));
        assertExpressionsEqual(new ConditionalExpression(FieldValueGetters.LAST_NAME, "J", ComparisonOperators.GREATER), exps.get(1));
        assertExpressionsEqual(new ConditionalExpression(FieldValueGetters.FIRST_NAME, "*a", ComparisonOperators.LIKE), exps.get(2));
    }

    @Test
    public void testQueryThrows() {
        // Wrong linking word
        assertThrows(IllegalArgumentException.class, () -> new QueryParser("jmbag=\"0123456789\"or"));
        // Missing whitespace after linking word
        assertThrows(IllegalArgumentException.class, () -> new QueryParser("jmbag=\"0123456789\"and"));
        assertThrows(IllegalArgumentException.class, () -> new QueryParser("jmbag=\"0123456789\"andlastName>\"J\""));

        // Missing field name
        assertThrows(IllegalArgumentException.class, () -> new QueryParser("=\"0123456789\""));
        // Invalid field name
        assertThrows(IllegalArgumentException.class, () -> new QueryParser("dateOfBirth=\"0123456789\""));

        // Missing operator
        assertThrows(IllegalArgumentException.class, () -> new QueryParser("jmbag\"0123456789\""));
        // Invalid operator
        assertThrows(IllegalArgumentException.class, () -> new QueryParser("jmbag<>\"0123456789\""));

        // EOF instead of field name
        assertThrows(IllegalArgumentException.class, () -> new QueryParser(""));
        assertThrows(IllegalArgumentException.class, () -> new QueryParser("jmbag=\"0123456789\"and "));
        // EOF instead of operator
        assertThrows(IllegalArgumentException.class, () -> new QueryParser("jmbag"));
        // EOF instead of string literal
        assertThrows(IllegalArgumentException.class, () -> new QueryParser("jmbag="));

        // Wrong or missing string delimiter
        assertThrows(IllegalArgumentException.class, () -> new QueryParser("jmbag= 0123456789"));
        assertThrows(IllegalArgumentException.class, () -> new QueryParser("jmbag= '0123456789'"));
        // Unterminated string literal
        assertThrows(IllegalArgumentException.class, () -> new QueryParser("jmbag=\"0123456789"));
    }

    private static void assertExpressionsEqual(ConditionalExpression expected, ConditionalExpression actual) {
        assertSame(expected.getFieldGetter(), actual.getFieldGetter());
        assertEquals(expected.getStringLiteral(), actual.getStringLiteral());
        assertSame(expected.getComparisonOperator(), actual.getComparisonOperator());
    }
}
