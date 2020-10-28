package hr.fer.oprpp1.math;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.junit.jupiter.api.Assertions.*;

class Vector2DTest {
    private static final double EPSILON = 1e-7;

    @ParameterizedTest
    @CsvFileSource(resources = "/Vector2D/constructor.csv")
    public void testConstructor(double x, double y) {
        Vector2D vector = new Vector2D(x, y);

        assertNotNull(vector);
        assertEquals(x, vector.getX(), EPSILON);
        assertEquals(y, vector.getY(), EPSILON);
    }

    @Test
    public void testConstructorWithNonfiniteArguments() {
        assertThrows(IllegalArgumentException.class, () -> new Vector2D(Double.POSITIVE_INFINITY, 0));
        assertThrows(IllegalArgumentException.class, () -> new Vector2D(Double.NEGATIVE_INFINITY, 0));
        assertThrows(IllegalArgumentException.class, () -> new Vector2D(Double.NaN, 0));
        assertThrows(IllegalArgumentException.class, () -> new Vector2D(0, Double.POSITIVE_INFINITY));
        assertThrows(IllegalArgumentException.class, () -> new Vector2D(0, Double.NEGATIVE_INFINITY));
        assertThrows(IllegalArgumentException.class, () -> new Vector2D(0, Double.NaN));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/Vector2D/add.csv")
    public void testAdd(double x1, double y1, double x2, double y2, double expectedX, double expectedY) {
        Vector2D vector1 = new Vector2D(x1, y1);
        vector1.add(new Vector2D(x2, y2));
        assertVectorEqual(expectedX, expectedY, vector1);

        Vector2D vector2 = new Vector2D(x2, y2);
        vector2.add(new Vector2D(x1, y1));
        assertVectorEqual(expectedX, expectedY, vector2);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/Vector2D/add.csv")
    public void testAdded(double x1, double y1, double x2, double y2, double expectedX, double expectedY) {
        Vector2D vector1 = new Vector2D(x1, y1).added(new Vector2D(x2, y2));
        assertVectorEqual(expectedX, expectedY, vector1);

        Vector2D vector2 = new Vector2D(x2, y2).added(new Vector2D(x1, y1));
        assertVectorEqual(expectedX, expectedY, vector2);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/Vector2D/rotate.csv")
    public void testRotate(double x, double y, double angle, double expectedX, double expectedY) {
        Vector2D vector = new Vector2D(x, y);
        vector.rotate(angle);
        assertVectorEqual(expectedX, expectedY, vector);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/Vector2D/rotate.csv")
    public void testRotated(double x, double y, double angle, double expectedX, double expectedY) {
        Vector2D vector = new Vector2D(x, y).rotated(angle);
        assertVectorEqual(expectedX, expectedY, vector);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/Vector2D/scale.csv")
    public void testScale(double x, double y, double scaler, double expectedX, double expectedY) {
        Vector2D vector = new Vector2D(x, y);
        vector.scale(scaler);
        assertVectorEqual(expectedX, expectedY, vector);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/Vector2D/scale.csv")
    public void testScaled(double x, double y, double scaler, double expectedX, double expectedY) {
        Vector2D vector = new Vector2D(x, y).scaled(scaler);
        assertVectorEqual(expectedX, expectedY, vector);
    }

    private void assertVectorEqual(double expectedX, double expectedY, Vector2D actual) {
        assertNotNull(actual);
        assertEquals(expectedX, actual.getX(), EPSILON);
        assertEquals(expectedY, actual.getY(), EPSILON);
    }
}
