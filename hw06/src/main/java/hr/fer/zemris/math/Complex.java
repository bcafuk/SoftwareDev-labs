package hr.fer.zemris.math;

import java.util.List;

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
     * Creates a new object representing the complex number 0.
     */
    public Complex() {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    /**
     * Creates a new object representing the complex number <i>{@code re}</i> + <i>{@code im} i</i>.
     *
     * @param re the real part of the number
     * @param im the imaginary part of the number
     * @throws IllegalArgumentException if {@code re} or {@code im} is not finite (&plusmn;infinity or NaN)
     */
    public Complex(double re, double im) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    /**
     * Calculates the number's magnitude, i.e. its modulus when written in polar coordinates.
     *
     * @return the magnitude, sqrt(Re(<i>z</i>)<sup>2</sup>&nbsp;+Im(<i>z</i>)<sup>2</sup>)
     * @see <a href="https://en.wikipedia.org/wiki/Complex_number#Modulus_and_argument">Modulus and argument of a complex number on Wikipedia</a>
     */
    public double module() {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    /**
     * Calculates the product of itself and <i>{@code c}</i>.
     *
     * @param c the complex number to be multiplied with {@code this}
     * @return <i>{@code this}</i> &middot; <i>{@code c}</i>
     * @throws NullPointerException if {@code c} is {@code null}
     */
    public Complex multiply(Complex c) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    /**
     * Calculates the quotient of itself and <i>{@code c}</i>.
     *
     * @param c the complex number by which to divide {@code this}
     * @return <i>{@code this}</i> / <i>{@code c}</i>
     * @throws NullPointerException if {@code c} is {@code null}
     */
    public Complex divide(Complex c) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    /**
     * Calculates the sum of itself and <i>{@code c}</i>.
     *
     * @param c the complex number which to add to {@code this}
     * @return <i>{@code this}</i> + <i>{@code c}</i>
     * @throws NullPointerException if {@code c} is {@code null}
     */
    public Complex add(Complex c) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    /**
     * Calculates the difference of itself and <i>{@code c}</i>.
     *
     * @param c the complex number which to subtract from {@code this}
     * @return <i>{@code this}</i> &minus; <i>{@code c}</i>
     * @throws NullPointerException if {@code c} is {@code null}
     */
    public Complex sub(Complex c) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    /**
     * Calculates the negative of itself.
     *
     * @return &minus;<i>{@code this}</i>
     */
    public Complex negate() {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    /**
     * Calculates the <i>{@code n}</i><sup>th</sup> power of itself.
     *
     * @param n the exponent to which to raise {@code this}; must be non-negative
     * @return <i>{@code this}</i><sup><i>{@code n}</i></sup>
     * @throws IllegalArgumentException if {@code n} is less than 0
     */
    public Complex power(int n) {
        throw new UnsupportedOperationException("Not yet implemented.");
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
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    /**
     * Returns the value of the complex number as a string of the format <code><i>re</i>+<i>im</i>i</code>
     *
     * @return a string representation of the number
     */
    @Override
    public String toString() {
        throw new UnsupportedOperationException("Not yet implemented.");
    }
}
