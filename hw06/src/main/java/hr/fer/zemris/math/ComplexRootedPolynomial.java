package hr.fer.zemris.math;

/**
 * Represents a complex polynomial in its factored form, i.e.
 * <i>f</i>(<i>z</i>) = <i>z</i><sub>0</sub> &middot; (<i>z</i>&minus;<i>z</i><sub>1</sub>) &middot;
 * (<i>z</i>&minus;<i>z</i><sub>2</sub>) &middot; &hellip; &middot; (<i>z</i>&minus;<i>z</i><sub><i>n</i></sub>).
 * <p>
 * This class is immutable, all operations create new objects and do not change any of their operands.
 */
public class ComplexRootedPolynomial {
    /**
     * Creates a new polynomial.
     *
     * @param constant the constant factor <i>z</i><sub>0</sub>
     * @param roots    the roots of the polynomial: <i>z</i><sub>1</sub>, <i>z</i><sub>2</sub>,
     *                 &hellip; <i>z</i><sub><i>n</i></sub>
     * @throws NullPointerException if {@code constant}, {@code roots}, or any element of {@code roots} is {@code null}
     */
    public ComplexRootedPolynomial(Complex constant, Complex... roots) {
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

    /**
     * Converts the factorized polynomial into its {@link ComplexPolynomial} form.
     *
     * @return the corresponding {@link ComplexPolynomial}
     */
    public ComplexPolynomial toComplexPolynom() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String toString() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Gets the index of the root closest to a given number, within a maximum distance, or &minus;1 if none was found.
     * <p>
     * The distance between two complex number is measured using the {@link Complex#module() modulus}
     * of their {@link Complex#sub(Complex) difference}.
     *
     * @param z        the number to which the closest root is to be found
     * @param treshold the maximum distance of the found root to <i>z</i>
     * @return the index of the root closest to {@code z} (0 for <i>z</i><sub>0</sub>, 1 for
     * <i>z</i><sub>2</sub>, &hellip; <i>k</i> &minus; 1 for <i>z</i><sub>k</sub>, &hellip;), or &minus;1 if none of
     * the roots' distance from  {@code z} is less than {@code treshold}
     */
    public int indexOfClosestRootFor(Complex z, double treshold) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
