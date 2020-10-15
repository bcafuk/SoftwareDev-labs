package hr.fer.oprpp1.hw01;

/**
 * An immutable complex number.
 *
 * @author Borna Cafuk
 */
public class ComplexNumber {
    /**
     * The real part of the number.
     */
    private final double real;
    /**
     * The imaginary part of the number.
     */
    private final double imaginary;

    /**
     * Constructs a new complex number with the given parts.
     *
     * @param real      the real part of the number
     * @param imaginary the imaginary part of the number
     */
    public ComplexNumber(double real, double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    /**
     * Constructs a complex number from a given real number.
     *
     * @param real the real number to convert from
     * @return a new complex number
     */
    public static ComplexNumber fromReal(double real) {
        return new ComplexNumber(real, 0);
    }

    /**
     * Constructs a complex number from a given imaginary number.
     *
     * @param imaginary the imaginary number's magnitude
     * @return a new complex number
     */
    public static ComplexNumber fromImaginary(double imaginary) {
        return new ComplexNumber(0, imaginary);
    }

    /**
     * Constructs a complex number from its polar coordinates.
     *
     * @param magnitude the number's magnitude (also called its modulus)
     * @param angle     the number's angle, in radians (also called its argument)
     * @return a new complex number
     */
    public static ComplexNumber fromMagnitudeAndAngle(double magnitude, double angle) {
        return new ComplexNumber(magnitude * Math.cos(angle), magnitude * Math.sin(angle));
    }

    /**
     * Parses a string and constructs a complex number.
     *
     * @param s the string to parse
     * @return a new complex number
     */
    public static ComplexNumber parse(String s) {
        // TODO: Implement parse method
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    /**
     * Gets the number's real part.
     *
     * @return the real part, Re(<i>z</i>)
     */
    public double getReal() {
        return real;
    }

    /**
     * Gets the number's imaginary part.
     *
     * @return the imaginary part, Im(<i>z</i>)
     */
    public double getImaginary() {
        return imaginary;
    }

    /**
     * Gets the number's magnitude, i.e. its modulus when written in polar coordinates.
     *
     * @return the magnitude, sqrt(Re(<i>z</i>)<sup>2</sup>&nbsp;+Im(<i>z</i>)<sup>2</sup>)
     * @see <a href="https://en.wikipedia.org/wiki/Complex_number#Modulus_and_argument">Modulus and argument of a complex number on Wikipedia</a>
     */
    public double getMagnitude() {
        return Math.hypot(real, imaginary);
    }

    /**
     * Gets the number's angle, i.e. its argument when written in polar coordinates.
     * <p>
     * The angle will be in radians and between 0 and 2 pi.
     *
     * @return the angle in radians
     * @see <a href="https://en.wikipedia.org/wiki/Complex_number#Modulus_and_argument">Modulus and argument of a complex number on Wikipedia</a>
     */
    public double getAngle() {
        double angle = Math.atan2(imaginary, real);

        // Math2.atan2 returns an angle between -pi and pi
        if (angle < 0)
            angle += 2 * Math.PI;

        return angle;
    }

    /**
     * Adds a complex number to itself and returns the result. Both of the operands remain unchanged.
     *
     * @param c the addend
     * @return a new complex number representing the sum
     */
    public ComplexNumber add(ComplexNumber c) {
        return new ComplexNumber(this.real + c.real, this.imaginary + c.imaginary);
    }

    /**
     * Subtracts a complex number from itself and returns the result. Both of the operands remain unchanged.
     *
     * @param c the number to be subtracted from {@code this}, the subtrahend
     * @return a new complex number representing the difference
     */
    public ComplexNumber sub(ComplexNumber c) {
        return new ComplexNumber(this.real - c.real, this.imaginary - c.imaginary);
    }

    /**
     * Multiplies a complex number to itself and returns the result. Both of the operands remain unchanged.
     *
     * @param c the multiplier
     * @return a new complex number representing the product
     */
    public ComplexNumber mul(ComplexNumber c) {
        return new ComplexNumber(
                this.real * c.real - this.imaginary * c.imaginary,
                this.real * c.imaginary + this.imaginary * c.real
        );
    }

    /**
     * Divides itself by a complex number and returns the result. Both of the operands remain unchanged.
     *
     * @param c the divisor
     * @return a new complex number representing the quotient
     */
    public ComplexNumber div(ComplexNumber c) {
        return this.mul(c.reciproc());
    }

    /**
     * Returns the reciprocal of a complex number. The complex number remains unchanged.
     *
     * @return a new complex number representing the reciprocal
     */
    private ComplexNumber reciproc() {
        double denominator = real * real + imaginary * imaginary;
        return new ComplexNumber(real / denominator, -imaginary / denominator);
    }

    /**
     * Raises itself to a power. The complex number remains unchanged.
     * <p>
     * The exponent must be greater than or equal to zero.
     *
     * @param n the exponent, must be >=&nbsp;0
     * @return a new complex number representing the result of the exponentiation
     * @throws IllegalArgumentException if {@code n} is negative
     */
    public ComplexNumber power(int n) {
        if (n < 0)
            throw new IllegalArgumentException("The exponent cannot be negative, but " + n + " was given.");

        return ComplexNumber.fromMagnitudeAndAngle(
                Math.pow(getMagnitude(), n),
                n * getAngle()
        );
    }

    /**
     * Calculates the <i>n</i><sup>th</sup> roots of itself. The complex number remains unchanged.
     *
     * @param n the degree of the root, must be >&nbsp;0
     * @return an array of {@code n} new complex numbers representing the of the {@code n}<sup>th</sup> roots
     * @throws IllegalArgumentException if {@code n} is 0 or negative
     */
    public ComplexNumber[] root(int n) {
        if (n <= 0)
            throw new IllegalArgumentException("The exponent must be positive, but " + n + " was given.");

        ComplexNumber[] roots = new ComplexNumber[n];

        double magnitude = Math.pow(getMagnitude(), 1.0 / n);
        double baseAngle = getAngle() / n;

        for (int i = 0; i < n; i++)
            roots[i] = ComplexNumber.fromMagnitudeAndAngle(
                    magnitude,
                    baseAngle + 2 * i * Math.PI / n
            );

        return roots;
    }

    /**
     * Returns the imaginary number's Cartesian notation as a string.
     *
     * @return the Cartesian notation
     */
    @Override
    public String toString() {
        if (imaginary == 0)
            return String.valueOf(real);

        if (real == 0)
            return imaginary + "i";

        StringBuilder sb = new StringBuilder();

        sb.append(real);

        // Special case: NaN is not less than zero
        if (!(imaginary < 0))
            sb.append('+');

        sb.append(imaginary).append('i');

        return sb.toString();
    }
}
