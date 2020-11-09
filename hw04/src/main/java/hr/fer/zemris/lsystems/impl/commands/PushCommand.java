package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;

/**
 * A command which duplicates the current state and pushes it onto the context.
 *
 * @author Borna Cafuk
 * @see Context
 */
public class PushCommand implements Command {
    /**
     * Pushes a copy of the current state onto the context.
     *
     * @param ctx     the turtle's context
     * @param painter a painter; unused
     */
    @Override
    public void execute(Context ctx, Painter painter) {
        ctx.pushState(ctx.getCurrentState().copy());
    }
}
