package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;

/**
 * A command which rotates the turtle.
 *
 * @author Borna Cafuk
 */
public class RotateCommand implements Command {
    /**
     * The angle <b>in radians</b> by which to rotate the turtle in the positive direction (counterclockwise)
     * when the {@link #execute(Context, Painter)} method is invoked.
     */
    private double angle;

    /**
     * Constructs a new command which rotates the turtle counterclockwise by the given amount <b>in degrees</b>.
     *
     * @param angleInDegrees the angle <b>in degrees</b> by which to rotate the turtle when executing the command
     */
    public RotateCommand(double angleInDegrees) {
        this.angle = angleInDegrees / 180 * Math.PI;
    }

    /**
     * Modifies the state at the top of the context by rotating the turtle.
     *
     * @param ctx     the turtle's context
     * @param painter a painter; unused
     */
    @Override
    public void execute(Context ctx, Painter painter) {
        ctx.getCurrentState().getDirection().rotate(angle);
    }
}
