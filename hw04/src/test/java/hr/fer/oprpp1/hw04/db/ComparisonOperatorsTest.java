package hr.fer.oprpp1.hw04.db;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.junit.jupiter.api.Assertions.*;

class ComparisonOperatorsTest {
    @ParameterizedTest
    @CsvFileSource(resources = "/comparisonOperatorTestCases/lesserStrings.csv")
    public void testLessWithDifferentStrings(String lesser, String greater) {
        assertTrue(ComparisonOperators.LESS.satisfied(lesser, greater));
        assertFalse(ComparisonOperators.LESS.satisfied(greater, lesser));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/comparisonOperatorTestCases/strings.csv")
    public void testLessWithSameStrings(String value) {
        assertFalse(ComparisonOperators.LESS.satisfied(value, value));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/comparisonOperatorTestCases/lesserStrings.csv")
    public void testLessOrEqualsWithDifferentStrings(String lesser, String greater) {
        assertTrue(ComparisonOperators.LESS_OR_EQUALS.satisfied(lesser, greater));
        assertFalse(ComparisonOperators.LESS_OR_EQUALS.satisfied(greater, lesser));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/comparisonOperatorTestCases/strings.csv")
    public void testLessOrEqualsWithSameStrings(String value) {
        assertTrue(ComparisonOperators.LESS_OR_EQUALS.satisfied(value, value));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/comparisonOperatorTestCases/lesserStrings.csv")
    public void testGreaterWithDifferentStrings(String lesser, String greater) {
        assertFalse(ComparisonOperators.GREATER.satisfied(lesser, greater));
        assertTrue(ComparisonOperators.GREATER.satisfied(greater, lesser));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/comparisonOperatorTestCases/strings.csv")
    public void testGreaterWithSameStrings(String value) {
        assertFalse(ComparisonOperators.GREATER.satisfied(value, value));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/comparisonOperatorTestCases/lesserStrings.csv")
    public void testGreaterOrEqualsWithDifferentStrings(String lesser, String greater) {
        assertFalse(ComparisonOperators.GREATER_OR_EQUALS.satisfied(lesser, greater));
        assertTrue(ComparisonOperators.GREATER_OR_EQUALS.satisfied(greater, lesser));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/comparisonOperatorTestCases/strings.csv")
    public void testGreaterOrEqualsWithSameStrings(String value) {
        assertTrue(ComparisonOperators.GREATER_OR_EQUALS.satisfied(value, value));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/comparisonOperatorTestCases/lesserStrings.csv")
    public void testEqualsWithDifferentStrings(String lesser, String greater) {
        assertFalse(ComparisonOperators.EQUALS.satisfied(lesser, greater));
        assertFalse(ComparisonOperators.EQUALS.satisfied(greater, lesser));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/comparisonOperatorTestCases/strings.csv")
    public void testEqualsWithSameStrings(String value) {
        assertTrue(ComparisonOperators.EQUALS.satisfied(value, value));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/comparisonOperatorTestCases/lesserStrings.csv")
    public void testNotEqualsWithDifferentStrings(String lesser, String greater) {
        assertTrue(ComparisonOperators.NOT_EQUALS.satisfied(lesser, greater));
        assertTrue(ComparisonOperators.NOT_EQUALS.satisfied(greater, lesser));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/comparisonOperatorTestCases/strings.csv")
    public void testNotEqualsWithSameStrings(String value) {
        assertFalse(ComparisonOperators.NOT_EQUALS.satisfied(value, value));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/comparisonOperatorTestCases/validPatterns.csv")
    public void testLikeReturnsTrue(String data, String pattern) {
        assertTrue(ComparisonOperators.LIKE.satisfied(data, pattern));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/comparisonOperatorTestCases/invalidPatterns.csv")
    public void testLikeReturnsFalse(String data, String pattern) {
        assertFalse(ComparisonOperators.LIKE.satisfied(data, pattern));
    }

    @Test
    public void testLikeThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> ComparisonOperators.LIKE.satisfied("data", "multiple*wildcards*"));
    }
}
