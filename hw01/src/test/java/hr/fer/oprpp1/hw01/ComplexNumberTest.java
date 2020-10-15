package hr.fer.oprpp1.hw01;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.junit.jupiter.api.Assertions.*;

class ComplexNumberTest {
    static final double DOUBLE_EPSILON = 1.0e-7;

    @ParameterizedTest
    @CsvFileSource(resources = "/finiteComplexNumbers.csv", numLinesToSkip = 1)
    public void constructor(double real, double imaginary) {
        ComplexNumber z = new ComplexNumber(real, imaginary);
        assertEquals(real, z.getReal(), DOUBLE_EPSILON);
        assertEquals(imaginary, z.getImaginary(), DOUBLE_EPSILON);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/nonfiniteComplexNumbers.csv", numLinesToSkip = 1)
    public void constructorNonfinite(double real, double imaginary) {
        ComplexNumber z = new ComplexNumber(real, imaginary);
        assertEquals(real, z.getReal(), DOUBLE_EPSILON);
        assertEquals(imaginary, z.getImaginary(), DOUBLE_EPSILON);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/finiteComplexNumbers.csv", numLinesToSkip = 1)
    public void fromReal(double real) {
        ComplexNumber z = ComplexNumber.fromReal(real);
        assertEquals(real, z.getReal(), DOUBLE_EPSILON);
        assertEquals(0.0, z.getImaginary(), DOUBLE_EPSILON);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/nonfiniteComplexNumbers.csv", numLinesToSkip = 1)
    public void fromRealNonfinite(double real) {
        ComplexNumber z = ComplexNumber.fromReal(real);
        assertEquals(real, z.getReal(), DOUBLE_EPSILON);
        assertEquals(0.0, z.getImaginary(), DOUBLE_EPSILON);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/finiteComplexNumbers.csv", numLinesToSkip = 1)
    public void fromImaginary(double real, double imaginary) {
        ComplexNumber z = ComplexNumber.fromImaginary(imaginary);
        assertEquals(0.0, z.getReal(), DOUBLE_EPSILON);
        assertEquals(imaginary, z.getImaginary(), DOUBLE_EPSILON);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/nonfiniteComplexNumbers.csv", numLinesToSkip = 1)
    public void fromImaginaryNonfinite(double real, double imaginary) {
        ComplexNumber z = ComplexNumber.fromImaginary(imaginary);
        assertEquals(0.0, z.getReal(), DOUBLE_EPSILON);
        assertEquals(imaginary, z.getImaginary(), DOUBLE_EPSILON);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/finiteComplexNumbers.csv", numLinesToSkip = 1)
    public void fromMagnitudeAndAngle(double real, double imaginary, double magnitude, double angle) {
        ComplexNumber z = ComplexNumber.fromMagnitudeAndAngle(magnitude, angle);
        assertEquals(real, z.getReal(), DOUBLE_EPSILON);
        assertEquals(imaginary, z.getImaginary(), DOUBLE_EPSILON);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/finiteComplexNumbers.csv", numLinesToSkip = 1)
    public void getMagnitude(double real, double imaginary, double magnitude) {
        ComplexNumber z = new ComplexNumber(real, imaginary);
        assertEquals(magnitude, z.getMagnitude(), DOUBLE_EPSILON);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/nonfiniteComplexNumbers.csv", numLinesToSkip = 1)
    public void getMagnitudeNonfinite(double real, double imaginary, double magnitude) {
        ComplexNumber z = new ComplexNumber(real, imaginary);
        assertEquals(magnitude, z.getMagnitude(), DOUBLE_EPSILON);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/finiteComplexNumbers.csv", numLinesToSkip = 1)
    public void getAngle(double real, double imaginary, double magnitude, double angle) {
        ComplexNumber z = new ComplexNumber(real, imaginary);
        assertEquals(angle, z.getAngle(), DOUBLE_EPSILON);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/nonfiniteComplexNumbers.csv", numLinesToSkip = 1)
    public void getAngleNonfinite(double real, double imaginary, double magnitude, double angle) {
        ComplexNumber z = new ComplexNumber(real, imaginary);
        assertEquals(angle, z.getAngle(), DOUBLE_EPSILON);
    }

    @Test
    public void addition() {
        ComplexNumber z1 = new ComplexNumber(3.5, 4.6);
        ComplexNumber z2 = new ComplexNumber(-6, 13);
        ComplexNumber z3 = new ComplexNumber(1e5, 1e5);
        ComplexNumber z4 = new ComplexNumber(1e-5, 1e-5);

        ComplexNumber z12 = z1.add(z2);
        assertEquals(-2.5, z12.getReal(), DOUBLE_EPSILON);
        assertEquals(17.6, z12.getImaginary(), DOUBLE_EPSILON);

        ComplexNumber z21 = z2.add(z1);
        assertEquals(-2.5, z21.getReal(), DOUBLE_EPSILON);
        assertEquals(17.6, z21.getImaginary(), DOUBLE_EPSILON);

        ComplexNumber z23 = z2.add(z3);
        assertEquals(99994, z23.getReal(), DOUBLE_EPSILON);
        assertEquals(100013, z23.getImaginary(), DOUBLE_EPSILON);

        ComplexNumber z14 = z1.add(z4);
        assertEquals(3.50001, z14.getReal(), DOUBLE_EPSILON);
        assertEquals(4.60001, z14.getImaginary(), DOUBLE_EPSILON);
    }

    @Test
    public void subtraction() {
        ComplexNumber z1 = new ComplexNumber(3.5, 4.6);
        ComplexNumber z2 = new ComplexNumber(-6, 13);
        ComplexNumber z3 = new ComplexNumber(1e5, 1e5);
        ComplexNumber z4 = new ComplexNumber(1e-5, 1e-5);

        ComplexNumber z12 = z1.sub(z2);
        assertEquals(9.5, z12.getReal(), DOUBLE_EPSILON);
        assertEquals(-8.4, z12.getImaginary(), DOUBLE_EPSILON);

        ComplexNumber z21 = z2.sub(z1);
        assertEquals(-9.5, z21.getReal(), DOUBLE_EPSILON);
        assertEquals(8.4, z21.getImaginary(), DOUBLE_EPSILON);

        ComplexNumber z23 = z2.sub(z3);
        assertEquals(-100006, z23.getReal(), DOUBLE_EPSILON);
        assertEquals(-99987, z23.getImaginary(), DOUBLE_EPSILON);

        ComplexNumber z14 = z1.sub(z4);
        assertEquals(3.49999, z14.getReal(), DOUBLE_EPSILON);
        assertEquals(4.59999, z14.getImaginary(), DOUBLE_EPSILON);
    }

    @Test
    public void multiplication() {
        ComplexNumber z1 = new ComplexNumber(3.5, 4.6);
        ComplexNumber z2 = new ComplexNumber(-6, 13);
        ComplexNumber z3 = new ComplexNumber(1e5, 1e5);
        ComplexNumber z4 = new ComplexNumber(1e-4, 1e-4);

        ComplexNumber z12 = z1.mul(z2);
        assertEquals(-80.8, z12.getReal(), DOUBLE_EPSILON);
        assertEquals(17.9, z12.getImaginary(), DOUBLE_EPSILON);

        ComplexNumber z21 = z2.mul(z1);
        assertEquals(-80.8, z21.getReal(), DOUBLE_EPSILON);
        assertEquals(17.9, z21.getImaginary(), DOUBLE_EPSILON);

        ComplexNumber z23 = z2.mul(z3);
        assertEquals(-1.9e6, z23.getReal(), DOUBLE_EPSILON);
        assertEquals(7e5, z23.getImaginary(), DOUBLE_EPSILON);

        ComplexNumber z14 = z1.mul(z4);
        assertEquals(-1.1e-4, z14.getReal(), DOUBLE_EPSILON);
        assertEquals(8.1e-4, z14.getImaginary(), DOUBLE_EPSILON);
    }

    @Test
    public void division() {
        ComplexNumber z1 = new ComplexNumber(3.5, 4.6);
        ComplexNumber z2 = new ComplexNumber(-6, 13);
        ComplexNumber z3 = new ComplexNumber(1e5, 1e5);
        ComplexNumber z4 = new ComplexNumber(1e-4, 1e-4);

        ComplexNumber z12 = z1.div(z2);
        assertEquals(0.18926829, z12.getReal(), DOUBLE_EPSILON);
        assertEquals(-0.35658537, z12.getImaginary(), DOUBLE_EPSILON);

        ComplexNumber z21 = z2.div(z1);
        assertEquals(1.16132894, z21.getReal(), DOUBLE_EPSILON);
        assertEquals(2.18796767, z21.getImaginary(), DOUBLE_EPSILON);

        ComplexNumber z23 = z2.div(z3);
        assertEquals(3.5e-5, z23.getReal(), DOUBLE_EPSILON);
        assertEquals(9.5e-5, z23.getImaginary(), DOUBLE_EPSILON);

        ComplexNumber z14 = z1.div(z4);
        assertEquals(4.05e4, z14.getReal(), DOUBLE_EPSILON);
        assertEquals(5.5e3, z14.getImaginary(), DOUBLE_EPSILON);
    }

    @Test
    public void power() {
        ComplexNumber z = new ComplexNumber(3.5, 4.6);

        ComplexNumber z0 = z.power(0);
        assertEquals(1, z0.getReal(), DOUBLE_EPSILON);
        assertEquals(0, z0.getImaginary(), DOUBLE_EPSILON);

        ComplexNumber z1 = z.power(1);
        assertEquals(3.5, z1.getReal(), DOUBLE_EPSILON);
        assertEquals(4.6, z1.getImaginary(), DOUBLE_EPSILON);

        ComplexNumber z2 = z.power(2);
        assertEquals(-8.91, z2.getReal(), DOUBLE_EPSILON);
        assertEquals(32.2, z2.getImaginary(), DOUBLE_EPSILON);

        ComplexNumber z10 = z.power(10);
        assertEquals(-40614994.92742015, z10.getReal(), DOUBLE_EPSILON);
        assertEquals(9126187.16571121, z10.getImaginary(), DOUBLE_EPSILON);
    }

    @Test
    public void roots() {
        ComplexNumber z = new ComplexNumber(-957.4519, -573.804);

        ComplexNumber[] root4 = z.root(4);
        assertEquals(4, root4.length);
        assertEquals(3.5, root4[0].getReal(), DOUBLE_EPSILON);
        assertEquals(4.6, root4[0].getImaginary(), DOUBLE_EPSILON);
        assertEquals(-4.6, root4[1].getReal(), DOUBLE_EPSILON);
        assertEquals(3.5, root4[1].getImaginary(), DOUBLE_EPSILON);
        assertEquals(-3.5, root4[2].getReal(), DOUBLE_EPSILON);
        assertEquals(-4.6, root4[2].getImaginary(), DOUBLE_EPSILON);
        assertEquals(4.6, root4[3].getReal(), DOUBLE_EPSILON);
        assertEquals(-3.5, root4[3].getImaginary(), DOUBLE_EPSILON);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/finiteComplexNumbers.csv", numLinesToSkip = 1)
    public void testToString(double real, double imaginary, double magnitude, double angle, String string) {
        assertEquals(string, new ComplexNumber(real, imaginary).toString());
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/nonfiniteComplexNumbers.csv", numLinesToSkip = 1)
    public void testToStringNonfinite(double real, double imaginary, double magnitude, double angle, String string) {
        assertEquals(string, new ComplexNumber(real, imaginary).toString());
    }
}
