package hr.fer.oprpp1.math;

import java.util.Objects;

/**
 * A vector with two real-valued components, <em>x</em> and <em>y</em>.
 *
 * @author Borna Cafuk
 */
public class Vector2D {
    /**
     * The vector's <em>x</em> component.
     */
    private double x;
    /**
     * The vector's <em>y</em> component.
     */
    private double y;

    /**
     * Constructs a new vector with the given components.
     *
     * @param x the <em>x</em> component
     * @param y the <em>y</em> component
     * @throws IllegalArgumentException if {@code x} or {@code y} is infinite or NaN
     */
    public Vector2D(double x, double y) {
        if (Double.isInfinite(x))
            throw new IllegalArgumentException("The x component can not be infinite.");
        if (Double.isNaN(x))
            throw new IllegalArgumentException("The x component can not be NaN.");
        if (Double.isInfinite(y))
            throw new IllegalArgumentException("The y component can not be infinite.");
        if (Double.isNaN(y))
            throw new IllegalArgumentException("The y component can not be NaN.");

        this.x = x;
        this.y = y;
    }

    /**
     * Gets the vector's <em>x</em> component.
     *
     * @return the <em>x</em> component
     */
    public double getX() {
        return x;
    }

    /**
     * Gets the vector's <em>y</em> component.
     *
     * @return the <em>y</em> component
     */
    public double getY() {
        return y;
    }

    /**
     * Changes a vector by adding another vector to it.
     *
     * @param offset the vector to add
     * @throws NullPointerException if {@code offset} is {@code null}
     */
    public void add(Vector2D offset) {
        Objects.requireNonNull(offset, "The offset must not be null.");

        x += offset.x;
        y += offset.y;
    }

    /**
     * Adds two vectors together without changing them.
     *
     * @param offset the vector to add
     * @return a new vector representing the sum of the two vectors
     * @throws NullPointerException if {@code offset} is {@code null}
     */
    public Vector2D added(Vector2D offset) {
        Vector2D result = copy();
        result.add(offset);
        return result;
    }

    /**
     * Changes a vector by rotating it.
     *
     * @param angle the angle by which to rotate
     * @throws IllegalArgumentException if {@code angle} is infinite or NaN
     */
    public void rotate(double angle) {
        if (Double.isInfinite(angle))
            throw new IllegalArgumentException("The angle can not be infinite.");
        if (Double.isNaN(angle))
            throw new IllegalArgumentException("The angle can not be NaN.");

        double oldX = x;
        double oldY = y;

        x = oldX * Math.cos(angle) - oldY * Math.sin(angle);
        y = oldX * Math.sin(angle) + oldY * Math.cos(angle);
    }

    /**
     * Rotates a vector without changing it.
     *
     * @param angle the angle by which to rotate
     * @return a new vector representing the result of the rotation
     * @throws IllegalArgumentException if {@code angle} is infinite or NaN
     */
    public Vector2D rotated(double angle) {
        Vector2D result = copy();
        result.rotate(angle);
        return result;
    }

    /**
     * Changes a vector by scaling it.
     *
     * @param scaler the factor by which to scale
     * @throws IllegalArgumentException if {@code scaler} is infinite or NaN
     */
    public void scale(double scaler) {
        if (Double.isInfinite(scaler))
            throw new IllegalArgumentException("The scaler can not be infinite.");
        if (Double.isNaN(scaler))
            throw new IllegalArgumentException("The scaler can not be NaN.");

        x *= scaler;
        y *= scaler;
    }

    /**
     * Scales a vector without changing it.
     *
     * @param scaler the factor by which to scale
     * @return a new vector representing the result of the scaling
     * @throws IllegalArgumentException if {@code scaler} is infinite or NaN
     */
    public Vector2D scaled(double scaler) {
        Vector2D result = copy();
        result.scale(scaler);
        return result;
    }

    /**
     * Constructs a new vector with the same components.
     *
     * @return the new vector
     */
    public Vector2D copy() {
        return new Vector2D(x, y);
    }
}
