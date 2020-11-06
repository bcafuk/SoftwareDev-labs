package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;

/**
 * A command which changes the turtle's effective scale.
 *
 * @author Borna Cafuk
 */
public class ScaleCommand implements Command {
    /**
     * The factor by which to multiply the turtle's effective scale
     * when the {@link #execute(Context, Painter)} method is invoked.
     */
    private double factor;

    /**
     * Constructs a new command which multiplies the turtle's effective scale by the given amount.
     *
     * @param factor the factor by which to multiply the turtle's effective scale
     */
    public ScaleCommand(double factor) {
        this.factor = factor;
    }

    /**
     * Modifies the state at the top of the context by scaling the effective scale.
     *
     * @param ctx     the turtle's context
     * @param painter a painter; unused
     */
    @Override
    public void execute(Context ctx, Painter painter) {
        ctx.getCurrentState().setEffectiveScale(ctx.getCurrentState().getEffectiveScale() * factor);
    }
}
