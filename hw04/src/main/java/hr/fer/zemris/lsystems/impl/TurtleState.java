package hr.fer.zemris.lsystems.impl;

import hr.fer.oprpp1.math.Vector2D;

import java.awt.*;

/**
 * Represents the state of a turtle: its position, direction, pen color and effective scale.
 *
 * @author Borna Cafuk
 */
public class TurtleState {
    /**
     * A position vector (a.k.a. a radius vector) representing the turtle's position.
     */
    private Vector2D position;

    /**
     * A direction vector (i.e. a unit vector) pointing in the direction the turtle is facing.
     */
    private Vector2D direction;

    /**
     * The current color of the turtle's pen.
     */
    private Color color;

    /**
     * A factor by which lengths of the turtle's moves are multiplied.
     * <p>
     * For example, if a turtle at (0,0) is facing in the positive x direction and has a scale of 0.2,
     * then a move of 0.5 will bring it to (0.1, 0).
     */
    private double effectiveScale;

    /**
     * Constructs a new turtle state with the given parameters.
     *
     * @param position       a position vector (a.k.a. a radius vector) representing the turtle's position
     * @param direction      a direction vector (i.e. a unit vector) representing the turtle's direction
     * @param color          the turtle's pen color
     * @param effectiveScale the turtle's effective scale (see {@link #effectiveScale})
     */
    public TurtleState(Vector2D position, Vector2D direction, Color color, double effectiveScale) {
        this.position = position;
        this.direction = direction;
        this.color = color;
        this.effectiveScale = effectiveScale;
    }

    /**
     * Creates a new turtle with the same parameters.
     *
     * @return a new turtle
     */
    public TurtleState copy() {
        return new TurtleState(position.copy(), direction.copy(), color, effectiveScale);
    }

    /**
     * Gets the turtle's current position.
     *
     * @return a position vector (a.k.a. a radius vector) representing the turtle's current position
     */
    public Vector2D getPosition() {
        return position;
    }

    /**
     * Sets the turtle's position.
     *
     * @param position a position vector (a.k.a. a radius vector) representing the turtle's new position
     */
    public void setPosition(Vector2D position) {
        this.position = position;
    }

    /**
     * Gets the turtle's current direction.
     *
     * @return a direction vector (i.e. a unit vector) representing the turtle's current direction
     */
    public Vector2D getDirection() {
        return direction;
    }

    /**
     * Sets the turtle's direction.
     *
     * @param direction a direction vector (i.e. a unit vector) representing the turtle's new direction
     */
    public void setDirection(Vector2D direction) {
        this.direction = direction;
    }

    /**
     * Gets the turtle's current pen color.
     *
     * @return the turtle's current pen color
     */
    public Color getColor() {
        return color;
    }

    /**
     * Sets the turtle's pen color.
     *
     * @param color the turtle's new pen color
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Gets the turtle's current effective scale.
     *
     * @return the turtle's current effective scale
     * @see #effectiveScale
     */
    public double getEffectiveScale() {
        return effectiveScale;
    }


    /**
     * Sets the turtle's  effective scale.
     *
     * @param effectiveScale the turtle's new effective scale
     * @see #effectiveScale
     */
    public void setEffectiveScale(double effectiveScale) {
        this.effectiveScale = effectiveScale;
    }
}
