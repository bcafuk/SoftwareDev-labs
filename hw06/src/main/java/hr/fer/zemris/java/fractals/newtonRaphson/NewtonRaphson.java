package hr.fer.zemris.java.fractals.newtonRaphson;

import hr.fer.zemris.math.Complex;
import hr.fer.zemris.math.ComplexPolynomial;
import hr.fer.zemris.math.ComplexRootedPolynomial;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Renders a fractal derived from Newton-Raphson iteration.
 *
 * @author Borna Cafuk
 */
public final class NewtonRaphson {
    /**
     * Don't let anyone instantiate this class.
     */
    private NewtonRaphson() {}

    /**
     * Fills rows [{@code ymin}, {@code ymax}) of an array with the closest root of {@code polynomial} after {@code iterMax}
     * iterations.
     *
     * @param polynomial the polynomial for which to iterate
     * @param reMin      the real part of the bottom left corner of the entire viewport
     * @param reMax      the real part of the top right corner of the entire viewport
     * @param imMin      the imaginary part of the bottom left corner of the entire viewport
     * @param imMax      the imaginary part of the top right corner of the entire viewport
     * @param width      the width of the viewport in pixels
     * @param height     the height of the entire viewport in pixels
     * @param ymin       the y coordinate of the top row of pixels to be calculated, inclusive
     * @param ymax       the y coordinate of the bottom row of pixels to be calculated, exclusive
     * @param iterMax    the maximum number of iterations to perform
     * @param convTresh  the maximum distance between <i>z</i><sub><i>n</i></sub> and
     *                   <i>z</i><sub><i>n</i> &minus; 1</sub> for which to stop iterating early
     * @param rootThresh the maximum distance between the final <i>z</i><sub><i>n</i></sub> and one of the roots
     * @param data       the array of colors to be filled in, in row-major order
     *                   and of size {@code width} &times; {@code height}
     * @param cancel     a flag to be used by the caller to cancel the calculation midway;
     *                   set to {@code true} while the method is running to return early
     * @throws NullPointerException if {@code polynomial}, {@code data}, or {@code cancel} is {@code null}
     */
    public static void calculate(ComplexRootedPolynomial polynomial,
                                 double reMin, double reMax, double imMin, double imMax,
                                 int width, int height, int ymin, int ymax,
                                 int iterMax, double convTresh, double rootThresh, short[] data, AtomicBoolean cancel) {
        Objects.requireNonNull(polynomial, "The polynomial must not be null");
        Objects.requireNonNull(data, "The data array must not be null");
        Objects.requireNonNull(cancel, "The cancel flag must not be null");

        ComplexPolynomial f = polynomial.toComplexPolynom();
        ComplexPolynomial fDerived = f.derive();

        int offset = ymin * width;
        for (int y = ymin; y < ymax; y++) {
            if (cancel.get())
                return;

            for (int x = 0; x < width; x++) {
                Complex zCurr = mapToComplexPlane(x, y, 0, width - 1, 0, height - 1,
                        reMin, reMax, imMin, imMax);
                Complex zPrev;

                int iter = 0;
                do {
                    Complex numerator = f.apply(zCurr);
                    Complex denominator = fDerived.apply(zCurr);
                    Complex fraction = numerator.divide(denominator);

                    zPrev = zCurr;
                    zCurr = zCurr.sub(fraction);
                    iter++;
                } while (iter < iterMax && zCurr.sub(zPrev).module() > convTresh);

                int index = polynomial.indexOfClosestRootFor(zCurr, rootThresh);
                data[offset++] = (short) (index + 1);
            }
        }
    }

    /**
     * Maps screen coordinates into the complex plane.
     *
     * @param x     the x screen coordinate
     * @param y     the y screen coordinate
     * @param xMin  the x coordinate corresponding to {@code reMin}
     * @param xMax  the x coordinate corresponding to {@code reMax}
     * @param yMin  the y coordinate corresponding to {@code imMax}
     * @param yMax  the y coordinate corresponding to {@code imMin}
     * @param reMin the real part corresponding to {@code xMin}
     * @param reMax the real part corresponding to {@code xMax}
     * @param imMin the imaginary part corresponding to {@code yMax}
     * @param imMax the imaginary part corresponding to {@code yMin}
     * @return the complex number corresponding to the ({@code x}, {@code y}) coordinates on the specified viewport
     * @throws IllegalArgumentException if {@code xMin} = {@code xMax}, if {@code yMin} = {@code yMax}, or if
     *                                  any of the {@code double} parameters are not finite (&plusmn;infinity or NaN)
     */
    private static Complex mapToComplexPlane(int x, int y, int xMin, int xMax, int yMin, int yMax,
                                             double reMin, double reMax, double imMin, double imMax) {
        double re = map(x, xMin, xMax, reMin, reMax);
        double im = map(y, yMax, yMin, imMin, imMax);

        return new Complex(re, im);
    }

    /**
     * Maps a number from one interval to another linearly.
     * <p>
     * {@code t} = {@code loFrom} maps the output to {@code loTo} and
     * {@code t} = {@code hiFrom} maps it to {@code hiTo}.
     * For all other values, linear inter- or extrapolation is performed.
     *
     * @param t      the number to be mapped
     * @param loFrom the bound of the input interval corresponding to {@code loTo} in the output interval
     * @param hiFrom the bound of the input interval corresponding to {@code hiTo} in the output interval
     * @param loTo   the bound of the output interval corresponding to {@code loFrom} in the input interval
     * @param hiTo   the bound of the output interval corresponding to {@code hiFrom} in the input interval
     * @return the result of the mapping
     */
    private static double map(double t, double loFrom, double hiFrom, double loTo, double hiTo) {
        return (t - loFrom) / (hiFrom - loFrom) * (hiTo - loTo) + loTo;
    }
}
