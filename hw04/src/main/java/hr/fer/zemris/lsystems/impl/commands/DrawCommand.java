package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.oprpp1.math.Vector2D;
import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;

/**
 * A command which moves a turtle and draws a line along the path it moved.
 *
 * @author Borna Cafuk
 */
public class DrawCommand implements Command {
    /**
     * The width of the lines drawn by the command.
     */
    private static final float LINE_WIDTH = 1;

    /**
     * Moving the turtle is delegated this command.
     */
    private SkipCommand skipCommand;

    /**
     * Constructs a new command which moves the turtle along its direction vector,
     * drawing a line along the path it traveled.
     *
     * @param step the distance by which the turtle will be moved
     */
    public DrawCommand(double step) {
        this.skipCommand = new SkipCommand(step);
    }

    /**
     * Modifies the state at the top of the context by moving the turtle.
     * Also draws a line between the turtle's positions before and after the move.
     *
     * @param ctx     the turtle's context
     * @param painter the painter to draw on
     */
    @Override
    public void execute(Context ctx, Painter painter) {
        Vector2D oldPosition = ctx.getCurrentState().getPosition().copy();
        skipCommand.execute(ctx, painter);
        Vector2D newPosition = ctx.getCurrentState().getPosition();

        painter.drawLine(oldPosition.getX(), oldPosition.getY(), newPosition.getX(), newPosition.getY(),
                ctx.getCurrentState().getColor(), LINE_WIDTH);
    }
}
