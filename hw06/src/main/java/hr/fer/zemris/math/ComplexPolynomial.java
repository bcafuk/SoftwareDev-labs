package hr.fer.zemris.math;

/**
 * Represents a complex polynomial in its standard form, i.e.
 * <i>f</i>(<i>z</i>) = <i>z</i><sub><i>n</i></sub> <i>z</i><sup><i>n</i></sup> +
 * <i>z</i><sub><i>n &minus; 1</i></sub> <i>z</i><sup><i>n &minus; 1</i></sup> + &hellip; +
 * <i>z</i><sub>2</sub> <i>z</i><sup>2</sup> + <i>z</i><sub>1</sub> <i>z</i><sup>1</sup> + <i>z</i><sub>0</sub>
 * <p>
 * This class is immutable, all operations create new objects and do not change any of their operands.
 */
public class ComplexPolynomial {
    /**
     * Creates a new polynomial.
     *
     * @param factors an array of coefficients, starting from the constant term, and in ascending order of the
     *                corresponding power: <i>z</i><sub>0</sub>, <i>z</i><sub>1</sub>, <i>z</i><sub>2</sub>,
     *                &hellip; <i>z</i><sub><i>n</i></sub>
     * @throws NullPointerException {@code factors} or any element of {@code factors} is {@code null}
     */
    public ComplexPolynomial(Complex... factors) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Returns the order of the polynomial.
     *
     * @return the order of the polynomial
     */
    public short order() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Calculates the product of itself and <i>{@code p}</i>.
     *
     * @param p the polynomial to be multiplied with {@code this}
     * @return the product of the two polynomials, <i>{@code this}</i> <i>{@code p}</i>
     * @throws NullPointerException if {@code p} is {@code null}
     * @see <a href="https://en.wikipedia.org/wiki/Polynomial#Multiplication">Polynomial multiplication on Wikipedia</a>
     */
    public ComplexPolynomial multiply(ComplexPolynomial p) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Calculates the first derivative of a polynomial <i>f</i>(<i>z</i>) with respect to <i>z</i>.
     *
     * @return the polynomial's derivative
     * @see <a href="https://en.wikipedia.org/wiki/Power_rule#Complex_power_functions">Power rule on Wikipedia</a>
     */
    public ComplexPolynomial derive() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Evaluates the polynomial <i>f</i>(<i>z</i>) for a given <i>z</i>.
     *
     * @param z the value for which to evaluate the polynomial
     * @return the polynomial's value, <i>f</i>(<i>z</i>)
     * @throws NullPointerException if {@code z} is {@code null}
     */
    public Complex apply(Complex z) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String toString() {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
