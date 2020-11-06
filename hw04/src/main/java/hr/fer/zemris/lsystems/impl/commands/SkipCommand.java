package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.oprpp1.math.Vector2D;
import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;
import hr.fer.zemris.lsystems.impl.TurtleState;

/**
 * A command which moves a turtle without drawing a trail behind it.
 *
 * @author Borna Cafuk
 */
public class SkipCommand implements Command {
    /**
     * The distance by which to move the turtle along its direction vector
     * when the {@link #execute(Context, Painter)} method is invoked.
     */
    private double step;

    /**
     * Constructs a new command which moves the turtle along its direction vector without drawing a line.
     *
     * @param step the distance by which the turtle will be moved
     */
    public SkipCommand(double step) {
        this.step = step;
    }

    /**
     * Modifies the state at the top of the context by moving the turtle.
     *
     * @param ctx     the turtle's context
     * @param painter a painter; unused
     */
    @Override
    public void execute(Context ctx, Painter painter) {
        TurtleState state = ctx.getCurrentState();
        Vector2D oldPosition = state.getPosition().copy();

        Vector2D difference = state.getDirection().scaled(step * state.getEffectiveScale());
        state.getPosition().add(difference);
        Vector2D newPosition = state.getPosition();
    }
}
