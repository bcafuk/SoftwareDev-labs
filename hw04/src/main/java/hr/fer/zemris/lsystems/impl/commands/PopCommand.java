package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;

/**
 * A command which pops the top state from the context.
 *
 * @author Borna Cafuk
 * @see Context
 */
public class PopCommand implements Command {
    /**
     * Removes the current state from the context.
     *
     * @param ctx     the turtle's context
     * @param painter a painter; unused
     */
    @Override
    public void execute(Context ctx, Painter painter) {
        ctx.popState();
    }
}
