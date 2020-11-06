package hr.fer.zemris.lsystems.impl;

import hr.fer.zemris.lsystems.Painter;

/**
 * Represents a command operating upon a {@link Context turtle context} and a {@link Painter}.
 *
 * @author Borna Cafuk
 */
public interface Command {
    /**
     * Executes the command.
     * <p>
     * The turtle's context is manipulated and optionally lines may be drawn on the {@code painter}.
     *
     * @param ctx     the turtle's context
     * @param painter the painter object to optionally draw on
     */
    void execute(Context ctx, Painter painter);
}
