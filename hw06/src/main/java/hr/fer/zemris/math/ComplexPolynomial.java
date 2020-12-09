package hr.fer.zemris.math;

import java.util.Arrays;
import java.util.Objects;

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
     * An array of coefficients in the order received in the constructor.
     * It will always have a length of at least 1.
     */
    private final Complex[] factors;

    /**
     * Creates a new polynomial.
     *
     * @param factors an array of coefficients, starting from the constant term, and in ascending order of the
     *                corresponding power: <i>z</i><sub>0</sub>, <i>z</i><sub>1</sub>, <i>z</i><sub>2</sub>,
     *                &hellip; <i>z</i><sub><i>n</i></sub>
     * @throws NullPointerException {@code factors} or any element of {@code factors} is {@code null}
     */
    public ComplexPolynomial(Complex... factors) {
        Objects.requireNonNull(factors, "The factors must not be null");
        for (Complex factor : factors)
            Objects.requireNonNull(factor, "All of the factors must not be null");

        if (factors.length == 0)
            this.factors = new Complex[]{Complex.ZERO};
        else
            this.factors = Arrays.copyOf(factors, factors.length);
    }

    /**
     * Returns the order of the polynomial.
     *
     * @return the order of the polynomial
     */
    public short order() {
        return (short) (factors.length - 1);
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
        Objects.requireNonNull(p, "p must not be null");

        Complex[] newFactors = new Complex[this.order() + p.order() + 1];
        Arrays.fill(newFactors, Complex.ZERO);

        for (int i = 0; i < this.factors.length; i++)
            for (int j = 0; j < p.factors.length; j++)
                newFactors[i + j] = newFactors[i + j].add(this.factors[i].multiply(p.factors[j]));

        return new ComplexPolynomial(newFactors);
    }

    /**
     * Calculates the first derivative of a polynomial <i>f</i>(<i>z</i>) with respect to <i>z</i>.
     *
     * @return the polynomial's derivative
     * @see <a href="https://en.wikipedia.org/wiki/Power_rule#Complex_power_functions">Power rule on Wikipedia</a>
     */
    public ComplexPolynomial derive() {
        Complex[] newFactors = new Complex[order()];

        for (int i = 0; i < newFactors.length; i++)
            newFactors[i] = factors[i + 1].multiply(new Complex(i + 1, 0));

        return new ComplexPolynomial(newFactors);
    }

    /**
     * Evaluates the polynomial <i>f</i>(<i>z</i>) for a given <i>z</i>.
     *
     * @param z the value for which to evaluate the polynomial
     * @return the polynomial's value, <i>f</i>(<i>z</i>)
     * @throws NullPointerException if {@code z} is {@code null}
     */
    public Complex apply(Complex z) {
        Objects.requireNonNull(z, "z must not be null");

        Complex value = Complex.ZERO;
        for (int i = 0; i < factors.length; i++)
            value = value.add(z.power(i).multiply(factors[i]));

        return value;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int i = factors.length - 1; i >= 0; i--) {
            sb.append('(')
              .append(factors[i])
              .append(')');

            if (i != 0)
                sb.append("*z^")
                  .append(i)
                  .append(" + ");
        }

        return sb.toString();
    }
}
