package hr.fer.zemris.math;

import java.util.Objects;

/**
 * Represents a complex polynomial in its factored form, i.e.
 * <i>f</i>(<i>z</i>) = <i>z</i><sub>0</sub> &middot; (<i>z</i>&minus;<i>z</i><sub>1</sub>) &middot;
 * (<i>z</i>&minus;<i>z</i><sub>2</sub>) &middot; &hellip; &middot; (<i>z</i>&minus;<i>z</i><sub><i>n</i></sub>).
 * <p>
 * This class is immutable, all operations create new objects and do not change any of their operands.
 */
public class ComplexRootedPolynomial {
    /**
     * The constant multiplier <i>z</i><sub>0</sub>.
     */
    private final Complex constant;
    /**
     * The roots of the polynomial: <i>z</i><sub>1</sub>, <i>z</i><sub>2</sub>, &hellip; <i>z</i><sub><i>n</i></sub>
     */
    private final Complex[] roots;

    /**
     * Creates a new polynomial.
     *
     * @param constant the constant factor <i>z</i><sub>0</sub>
     * @param roots    the roots of the polynomial: <i>z</i><sub>1</sub>, <i>z</i><sub>2</sub>,
     *                 &hellip; <i>z</i><sub><i>n</i></sub>
     * @throws NullPointerException if {@code constant}, {@code roots}, or any element of {@code roots} is {@code null}
     */
    public ComplexRootedPolynomial(Complex constant, Complex... roots) {
        Objects.requireNonNull(constant, "The constant must not be null");
        Objects.requireNonNull(roots, "The roots must not be null");
        for (Complex root : roots)
            Objects.requireNonNull(root, "All of the roots must not be null");

        this.constant = constant;
        this.roots = roots;
    }

    /**
     * Returns the order of the polynomial.
     *
     * @return the order of the polynomial
     */
    public short order() {
        return (short) (roots.length);
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

        Complex value = constant;
        for (Complex root : roots)
            value = value.multiply(z.sub(root));

        return value;
    }

    /**
     * Converts the factorized polynomial into its {@link ComplexPolynomial} form.
     *
     * @return the corresponding {@link ComplexPolynomial}
     */
    public ComplexPolynomial toComplexPolynom() {
        ComplexPolynomial result = new ComplexPolynomial(constant);

        for (Complex root : roots)
            result = result.multiply(new ComplexPolynomial(root, Complex.ONE));

        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append('(')
          .append(constant)
          .append(')');

        for (Complex root : roots)
            sb.append(" * (z-(")
              .append(root)
              .append("))");

        return sb.toString();
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
     * @throws NullPointerException     if {@code z} is {@code null}
     * @throws IllegalArgumentException if {@code treshold} is NaN
     */
    public int indexOfClosestRootFor(Complex z, double treshold) {
        Objects.requireNonNull(z, "z must not be null");
        if (Double.isNaN(treshold))
            throw new IllegalArgumentException("The threshold must not be NaN");

        double minDist = Double.POSITIVE_INFINITY;
        int closestIndex = -1;

        for (int i = 0; i < roots.length; i++) {
            double dist = z.sub(roots[i]).module();

            if (dist < minDist) {
                minDist = dist;
                closestIndex = i;
            }
        }

        if (minDist <= treshold)
            return closestIndex;
        else
            return -1;
    }
}
