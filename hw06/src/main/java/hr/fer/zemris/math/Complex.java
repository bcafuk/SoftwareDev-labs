package hr.fer.zemris.math;

import java.util.List;
import java.util.Objects;

/**
 * A complex number.
 * <p>
 * This class is immutable, all operations create new objects and do not change any of their operands.
 *
 * @author Borna Cafuk
 */
public class Complex {
    /**
     * The complex number 0.
     */
    public static final Complex ZERO = new Complex(0, 0);
    /**
     * The complex number 1.
     */
    public static final Complex ONE = new Complex(1, 0);
    /**
     * The complex number &minus;1.
     */
    public static final Complex ONE_NEG = new Complex(-1, 0);
    /**
     * The complex number <i>i</i>.
     */
    public static final Complex IM = new Complex(0, 1);
    /**
     * The complex number &minus;<i>i</i>.
     */
    public static final Complex IM_NEG = new Complex(0, -1);

    /**
     * The number's real part.
     */
    private final double re;
    /**
     * The number's imaginary part.
     */
    private final double im;

    /**
     * Creates a new object representing the complex number 0.
     */
    public Complex() {
        this(0, 0);
    }

    /**
     * Creates a new object representing the complex number <i>{@code re}</i> + <i>{@code im} i</i>.
     *
     * @param re the real part of the number
     * @param im the imaginary part of the number
     * @throws IllegalArgumentException if {@code re} or {@code im} is not finite (&plusmn;infinity or NaN)
     */
    public Complex(double re, double im) {
        if (!Double.isFinite(re))
            throw new IllegalArgumentException("The real part has to be finite, but was " + re);
        if (!Double.isFinite(im))
            throw new IllegalArgumentException("The imaginary part has to be finite, but was " + im);

        this.re = re;
        this.im = im;
    }

    /**
     * Calculates the number's magnitude, i.e. its modulus when written in polar coordinates.
     *
     * @return the magnitude, sqrt(Re(<i>z</i>)<sup>2</sup>&nbsp;+Im(<i>z</i>)<sup>2</sup>)
     * @see <a href="https://en.wikipedia.org/wiki/Complex_number#Modulus_and_argument">Modulus and argument of a complex number on Wikipedia</a>
     */
    public double module() {
        return Math.hypot(re, im);
    }

    /**
     * Calculates the product of itself and <i>{@code c}</i>.
     *
     * @param c the complex number to be multiplied with {@code this}
     * @return <i>{@code this}</i> &middot; <i>{@code c}</i>
     * @throws NullPointerException if {@code c} is {@code null}
     */
    public Complex multiply(Complex c) {
        Objects.requireNonNull(c, "The parameter must not be null");

        double re = this.re * c.re - this.im * c.im;
        double im = this.re * c.im + this.im * c.re;
        return new Complex(re, im);
    }

    /**
     * Calculates the quotient of itself and <i>{@code c}</i>.
     *
     * @param c the complex number by which to divide {@code this}
     * @return <i>{@code this}</i> / <i>{@code c}</i>
     * @throws NullPointerException     if {@code c} is {@code null}
     * @throws IllegalArgumentException if {@code c} is 0
     */
    public Complex divide(Complex c) {
        Objects.requireNonNull(c, "The parameter must not be null");

        double cModulo2 = c.re * c.re + c.im * c.im;
        if (cModulo2 == 0.0)
            throw new IllegalArgumentException("The denominator must not be 0");

        double re = (this.re * c.re + this.im * c.im) / cModulo2;
        double im = (this.im * c.re - this.re * c.im) / cModulo2;
        return new Complex(re, im);
    }

    /**
     * Calculates the sum of itself and <i>{@code c}</i>.
     *
     * @param c the complex number which to add to {@code this}
     * @return <i>{@code this}</i> + <i>{@code c}</i>
     * @throws NullPointerException if {@code c} is {@code null}
     */
    public Complex add(Complex c) {
        Objects.requireNonNull(c, "The parameter must not be null");

        return new Complex(this.re + c.re, this.im + c.im);
    }

    /**
     * Calculates the difference of itself and <i>{@code c}</i>.
     *
     * @param c the complex number which to subtract from {@code this}
     * @return <i>{@code this}</i> &minus; <i>{@code c}</i>
     * @throws NullPointerException if {@code c} is {@code null}
     */
    public Complex sub(Complex c) {
        Objects.requireNonNull(c, "The parameter must not be null");

        return new Complex(this.re - c.re, this.im - c.im);
    }

    /**
     * Calculates the negative of itself.
     *
     * @return &minus;<i>{@code this}</i>
     */
    public Complex negate() {
        return new Complex(-this.re, -this.im);
    }

    /**
     * Calculates the <i>{@code n}</i><sup>th</sup> power of itself.
     *
     * @param n the exponent to which to raise {@code this}; must be non-negative
     * @return <i>{@code this}</i><sup><i>{@code n}</i></sup>
     * @throws IllegalArgumentException if {@code n} is less than 0
     */
    public Complex power(int n) {
        if (n < 0)
            throw new IllegalArgumentException("The exponent must be non-negative, but was " + n);

        // This method utilises recursion to calculate the power in O(log n) time.
        // Simple iteration would instead be O(n).

        if (n == 0) {
            // z^0 = 1
            // Note: it was an intentional and conscious decision to make 0^0 = 1.
            return ONE;
        }

        if (n == 1) {
            // z^1 = z
            // This isn't strictly necessary, but why not take a shortcut if the return value is already available?
            return this;
        }

        if (n % 2 == 1) {
            // For odd values of n:
            // z^n = (z*z) * z^((n-1)/2) * z
            return this.multiply(this).power(n / 2).multiply(this);
        } else {
            // For even values of n:
            // z^n = (z*z) * z^(n/2)
            return this.multiply(this).power(n / 2);
        }
    }

    /**
     * Calculates the <i>{@code n}</i><sup>th</sup> roots of itself.
     *
     * @param n the degree of the root; must be positive
     * @return The solutions <i>z</i> of the equation <i>z</i> = <i>{@code this}</i><sup><i>{@code n}</i></sup>
     * @throws IllegalArgumentException if {@code n} is less than 1
     * @see <a href="https://en.wikipedia.org/wiki/Nth_root#nth_roots">nth roots of a complex number on Wikipedia</a>
     */
    public List<Complex> root(int n) {
        if (n <= 0)
            throw new IllegalArgumentException("The exponent must be positive, but was " + n);

        Complex[] roots = new Complex[n];

        double modulus = Math.pow(Math.hypot(this.re, this.im), 1.0 / n);
        double initialArgument = Math.atan2(this.im, this.re);

        for (int i = 0; i < n; i++) {
            double argument = (initialArgument + 2 * i * Math.PI) / n;
            roots[i] = new Complex(modulus * Math.cos(argument), modulus * Math.sin(argument));
        }

        return List.of(roots);
    }

    /**
     * Returns the value of the complex number as a string of the format <code><i>re</i>&plusmn;<i>im</i>i</code>
     *
     * @return a string representation of the number
     */
    @Override
    public String toString() {
        return switch ((int) Math.signum(im)) {
            case -1 -> re + "" + im + "i";
            case 0 -> re + "+0.0i";
            case 1 -> re + "+" + im + "i";
            default -> throw new IllegalStateException();
        };
    }
}
