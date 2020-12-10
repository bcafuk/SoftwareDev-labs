package hr.fer.zemris.java.fractals;

import hr.fer.zemris.java.fractals.newtonRaphson.NewtonRaphson;
import hr.fer.zemris.java.fractals.util.RootInput;
import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;
import hr.fer.zemris.math.Complex;
import hr.fer.zemris.math.ComplexRootedPolynomial;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * An application to draw a fractal based on Newton-Raphson iteration.
 * <p>
 * This version of the application calculates the pixel values on a single thread.
 *
 * @author Borna Cafuk
 */
public class Newton {
    /**
     * The main program.
     *
     * @param args unused
     */
    public static void main(String[] args) {
        System.out.println("Welcome to Newton-Raphson iteration-based fractal viewer.");

        List<Complex> roots = null;
        try {
            roots = RootInput.readPolynomial(System.in, System.out);
        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        ComplexRootedPolynomial polynomial = new ComplexRootedPolynomial(Complex.ONE, roots.toArray(new Complex[0]));

        System.out.println("Image of fractal will appear shortly. Thank you.");
        FractalViewer.show(new NewtonProducer(polynomial));
    }

    /**
     * The fractal producer used to calculate the pixel values.
     */
    public static class NewtonProducer implements IFractalProducer {
        /**
         * The maximum number of iterations to be make when calculating a single pixel.
         */
        private static final int MAX_ITERATIONS = 1024;
        /**
         * The maximum distance between two consecutive values when iterating.
         * If the distance is smaller, iteration is stopped early.
         */
        private static final double CONVERGENCE_THRESHOLD = 1e-3;
        /**
         * The maximum allowed distance between the final value when iterating and the closest root.
         * If the closest root is farther from the final value, the point is treated as not being near any root.
         */
        private static final double ROOT_THRESHOLD = 2e-3;

        /**
         * The polynomial function used to draw the fractal.
         */
        private final ComplexRootedPolynomial polynomial;

        /**
         * Constructs a producer for the given polynomial.
         *
         * @param polynomial the polynomial for which to draw a fractal
         * @throws NullPointerException if {@code polynomial} is {@code null}
         */
        public NewtonProducer(ComplexRootedPolynomial polynomial) {
            this.polynomial = Objects.requireNonNull(polynomial, "The polynomial must not be null");
        }

        @Override
        public void produce(double reMin, double reMax, double imMin, double imMax, int width, int height,
                            long requestNo, IFractalResultObserver observer, AtomicBoolean cancel) {
            int m = polynomial.order() + 1;
            short[] data = new short[width * height];

            NewtonRaphson.calculate(polynomial, reMin, reMax, imMin, imMax, width, height, 0, height,
                    MAX_ITERATIONS, CONVERGENCE_THRESHOLD, ROOT_THRESHOLD, data, cancel);

            observer.acceptResult(data, (short) m, requestNo);
        }
    }
}
