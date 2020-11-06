package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;

import java.awt.*;

/**
 * A command which changes the turtle's pen color.
 *
 * @author Borna Cafuk
 */
public class ColorCommand implements Command {
    /**
     * The color to which the turtle's pen color will be changed
     * when the {@link #execute(Context, Painter)} method is invoked.
     */
    private Color color;

    /**
     * Constructs a new command which changes the turtle's pen color to the given color.
     *
     * @param color the new pen color to use
     */
    public ColorCommand(Color color) {
        this.color = color;
    }

    /**
     * Modifies the state at the top of the context by setting the pen color.
     *
     * @param ctx     the turtle's context
     * @param painter a painter; unused
     */
    @Override
    public void execute(Context ctx, Painter painter) {
        ctx.getCurrentState().setColor(color);
    }
}
