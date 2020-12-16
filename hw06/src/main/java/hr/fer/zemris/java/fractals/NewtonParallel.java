package hr.fer.zemris.java.fractals;

import hr.fer.zemris.java.fractals.newtonRaphson.NewtonRaphson;
import hr.fer.zemris.java.fractals.util.ArgumentParser;
import hr.fer.zemris.java.fractals.util.RootInput;
import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;
import hr.fer.zemris.math.Complex;
import hr.fer.zemris.math.ComplexRootedPolynomial;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * An application to draw a fractal based on Newton-Raphson iteration.
 * <p>
 * This version of the application calculates the pixel values using a thread pool.
 * See {@link Newton} for a single-threaded version.
 *
 * @author Borna Cafuk
 * @see Newton
 */
public class NewtonParallel {
    /**
     * The main program.
     * <p>
     * Accepted arguments are:
     * <dl>
     *     <dt>{@code --workers=N} or {@code -w N}</dt>
     *     <dd>The number of workers, defaults to the number of processors ({@link Runtime#availableProcessors()})</dd>
     *     <dt>{@code --tracks=N} or {@code -t N}</dt>
     *     <dd>The number of jobs to split the image into, defaults to four time the number of processors.</dd>
     * </dl>
     *
     * @param args the program arguments
     */
    public static void main(String[] args) {
        Runtime runtime = Runtime.getRuntime();
        int numberOfAvailableProcessors = runtime.availableProcessors();

        ArgumentParser<ArgumentType> argumentParser = new ArgumentParser<>();

        argumentParser.registerType("workers", "w", ArgumentType.WORKERS);
        argumentParser.registerType("tracks", "t", ArgumentType.TRACKS);

        Map<ArgumentType, Integer> arguments = null;
        try {
            arguments = argumentParser.parseArguments(args);
        } catch (IllegalArgumentException e) {
            System.err.println("Argument parsing error: " + e.getMessage());
            runtime.exit(1);
        }

        int workerCount = arguments.getOrDefault(ArgumentType.WORKERS, numberOfAvailableProcessors);
        int trackCount = arguments.getOrDefault(ArgumentType.TRACKS, 4 * numberOfAvailableProcessors);

        if (workerCount <= 0) {
            System.err.println("The number of workers has to be greater than 0.");
            runtime.exit(1);
        }
        if (trackCount <= 0) {
            System.err.println("The number of tracks has to be greater than 0.");
            runtime.exit(1);
        }

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
        FractalViewer.show(new NewtonProducer(polynomial, workerCount, trackCount));
    }

    /**
     * The type of the argument for use with {@link ArgumentParser}.
     */
    private enum ArgumentType {
        WORKERS,
        TRACKS,
    }

    /**
     * A job to which calculates a band of a fractal image.
     */
    public static class CalculationJob implements Runnable {
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

        ComplexRootedPolynomial polynomial;
        double reMin;
        double reMax;
        double imMin;
        double imMax;
        int width;
        int height;
        int ymin;
        int ymax;
        int iterMax;
        short[] data;
        AtomicBoolean cancel;

        /**
         * Poison pill.
         */
        public static CalculationJob NO_JOB = new CalculationJob();

        /**
         * Used to create the poison pill.
         */
        private CalculationJob() {}

        /**
         * Creates a calculation job
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
         * @param data       the array of colors to be filled in, in row-major order
         *                   and of size {@code width} &times; {@code height}
         * @param cancel     a flag to be used by the caller to cancel the calculation midway;
         *                   set to {@code true} while the method is running to return early
         * @throws NullPointerException if {@code polynomial}, {@code data}, or {@code cancel} is {@code null}
         */
        public CalculationJob(ComplexRootedPolynomial polynomial,
                              double reMin, double reMax, double imMin, double imMax,
                              int width, int height, int ymin, int ymax,
                              int iterMax, short[] data, AtomicBoolean cancel) {
            Objects.requireNonNull(polynomial, "The polynomial must not be null");
            Objects.requireNonNull(data, "The data array must not be null");
            Objects.requireNonNull(cancel, "The cancel flag must not be null");

            this.polynomial = polynomial;
            this.reMin = reMin;
            this.reMax = reMax;
            this.imMin = imMin;
            this.imMax = imMax;
            this.width = width;
            this.height = height;
            this.ymin = ymin;
            this.ymax = ymax;
            this.iterMax = iterMax;
            this.data = data;
            this.cancel = cancel;
        }

        @Override
        public void run() {
            NewtonRaphson.calculate(polynomial, reMin, reMax, imMin, imMax,
                    width, height, ymin, ymax, iterMax, CONVERGENCE_THRESHOLD, ROOT_THRESHOLD, data, cancel);
        }
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
         * The polynomial function used to draw the fractal.
         */
        private final ComplexRootedPolynomial polynomial;
        /**
         * The number of workers to be used in the worker pool.
         */
        private final int workerCount;
        /**
         * The number of horizontal bands to be used as jobs for the workers.
         */
        private final int trackCount;

        /**
         * Constructs a producer for the given polynomial.
         *
         * @param polynomial the polynomial for which to draw a fractal
         * @throws NullPointerException     if {@code polynomial} is {@code null}
         * @throws IllegalArgumentException if {@code workerCount} or {@code trackCount} is greater than 0
         */
        public NewtonProducer(ComplexRootedPolynomial polynomial, int workerCount, int trackCount) {
            Objects.requireNonNull(polynomial, "The polynomial must not be null");
            if (workerCount <= 0)
                throw new IllegalArgumentException("The worker count must be positive, but was " + workerCount);
            if (trackCount <= 0)
                throw new IllegalArgumentException("The track count must be positive, but was " + workerCount);

            this.polynomial = polynomial;
            this.workerCount = workerCount;
            this.trackCount = trackCount;
        }


        @Override
        public void produce(double reMin, double reMax, double imMin, double imMax, int width, int height,
                            long requestNo, IFractalResultObserver observer, AtomicBoolean cancel) {
            int m = polynomial.order() + 1;
            short[] data = new short[width * height];

            final int effectiveTrackCount = Math.min(trackCount, height);
            int trackHeight = height / effectiveTrackCount;

            System.out.format("Drawing image... (%d threads, %d jobs)%n", workerCount, trackCount);

            final BlockingQueue<CalculationJob> queue = new LinkedBlockingQueue<>();

            Runnable threadJob = () -> {
                while (true) {
                    CalculationJob p;
                    try {
                        p = queue.take();
                        if (p == CalculationJob.NO_JOB)
                            break;
                    } catch (InterruptedException e) {
                        continue;
                    }
                    p.run();
                }
            };

            Thread[] workers = new Thread[workerCount];
            for (int i = 0; i < workers.length; i++) {
                workers[i] = new Thread(threadJob);
            }

            for (Thread worker : workers)
                worker.start();

            for (int i = 0; i < effectiveTrackCount; i++) {
                int yMin = i * trackHeight;
                int yMax = (i + 1) * trackHeight;
                if (i == effectiveTrackCount - 1)
                    yMax = height;

                CalculationJob job = new CalculationJob(polynomial, reMin, reMax, imMin, imMax,
                        width, height, yMin, yMax, MAX_ITERATIONS, data, cancel);

                while (true) {
                    try {
                        queue.put(job);
                        break;
                    } catch (InterruptedException ignored) {
                    }
                }
            }

            for (int i = 0; i < workers.length; i++) {
                while (true) {
                    try {
                        queue.put(CalculationJob.NO_JOB);
                        break;
                    } catch (InterruptedException ignored) {
                    }
                }
            }

            for (Thread worker : workers) {
                while (true) {
                    try {
                        worker.join();
                        break;
                    } catch (InterruptedException ignored) {
                    }
                }
            }

            observer.acceptResult(data, (short) m, requestNo);
        }
    }
}
