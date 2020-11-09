package hr.fer.zemris.lsystems.impl;

import hr.fer.oprpp1.custom.collections.Dictionary;
import hr.fer.oprpp1.math.Vector2D;
import hr.fer.zemris.lsystems.LSystem;
import hr.fer.zemris.lsystems.LSystemBuilder;
import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.commands.*;

import java.awt.*;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A builder for an LSystem, as defined in the assignment document.
 *
 * @author Borna Cafuk
 */
public class LSystemBuilderImpl implements LSystemBuilder {
    /**
     * The productions used to expand the command string in each iteration of {@link LSystem#generate(int)}.
     */
    private Dictionary<Character, String> productions = new Dictionary<>();
    /**
     * A dictionary mapping characters in a command string to actual commands.
     */
    private Dictionary<Character, Command> commands = new Dictionary<>();

    /**
     * A factor used to scale the initial effective scale of the turtle.
     *
     * @see #unitLengthDegreeScaler
     */
    private double unitLength = 0.1;
    /**
     * The base of an exponential factor used to scale the initial effective scale of the turtle.
     * <p>
     * For a given generation degree <i>d</i>, the initial effective scale will be set to
     * <i>{@link #unitLength}</i> &times;<i>{@code unitLengthDegreeScaler}</i><sup><i>d</i></sup>.
     */
    private double unitLengthDegreeScaler = 1;
    /**
     * The initial position of the turtle.
     */
    private Vector2D origin = new Vector2D(0, 0);
    /**
     * The initial direction of the turtle <b>in degrees</b>.
     * <p>
     * <ul>
     *     <li>0 points right,</li>
     *     <li>90 points up,</li>
     *     <li>180 points left,</li>
     *     <li>270 points down,</li>
     *     <li>etc.</li>
     * </ul>
     */
    private double angle = 0;
    /**
     * The initial axiom of the L-system.
     *
     * @see <a href="https://en.wikipedia.org/wiki/L-system#L-system_structure">Wikipedia article on L-systems</a>
     */
    private String axiom = "";

    /**
     * Sets the unit length of the builder.
     *
     * @param unitLength the new unit length
     * @return {@code this}
     * @see #unitLength
     * @see #unitLengthDegreeScaler
     */
    @Override
    public LSystemBuilder setUnitLength(double unitLength) {
        this.unitLength = unitLength;
        return this;
    }

    /**
     * Sets the origin of the builder.
     *
     * @param x the new origin point's <em>x</em> coordinate
     * @param y the new origin point's <em>y</em> coordinate
     * @return {@code this}
     */
    @Override
    public LSystemBuilder setOrigin(double x, double y) {
        this.origin = new Vector2D(x, y);
        return this;
    }

    /**
     * Sets the initial angle of the builder.
     *
     * @param angle the new initial angle <b>in degrees</b>.
     * @return {@code this}
     */
    @Override
    public LSystemBuilder setAngle(double angle) {
        this.angle = angle;
        return this;
    }

    /**
     * Sets the axiom of the builder.
     *
     * @param axiom the new axiom
     * @return {@code this}
     * @see #axiom
     */
    @Override
    public LSystemBuilder setAxiom(String axiom) {
        this.axiom = axiom;
        return this;
    }

    /**
     * Sets the unit length degree scaler of the builder.
     *
     * @param unitLengthDegreeScaler the new unit length degree scaler
     * @return {@code this}
     * @see #unitLengthDegreeScaler
     */
    @Override
    public LSystemBuilder setUnitLengthDegreeScaler(double unitLengthDegreeScaler) {
        this.unitLengthDegreeScaler = unitLengthDegreeScaler;
        return this;
    }

    /**
     * Registers a new production.
     *
     * @param symbol     the symbol representing the left side of the production
     * @param production a string representing the right side of the production
     * @return {@code this}
     * @throws NullPointerException if {@code production} is {@code null}
     */
    @Override
    public LSystemBuilder registerProduction(char symbol, String production) {
        Objects.requireNonNull(production, "The production string must not be null.");

        this.productions.put(symbol, production);
        return this;
    }

    /**
     * Registers a new command.
     * <p>
     * Allowed commands are:
     * <ul>
     *     <li>{@code draw} <em>s</em>: registers a {@code DrawCommand}, where <em>s</em> is the step</li>
     *     <li>{@code skip} <em>s</em>: registers a {@code SkipCommand}, where <em>s</em> is the step</li>
     *     <li>{@code scale} <em>s</em>: registers a {@code ScaleCommand}, where <em>s</em> is the scale</li>
     *     <li>{@code rotate} <em>a</em>: registers a {@code Rotate}, where <em>a</em> is the angle</li>
     *     <li>{@code push}: registers a {@code PushCommand}</li>
     *     <li>{@code pop}: registers a {@code PopCommand}</li>
     *     <li>{@code color} <em>rrggbb</em>: registers a {@code ColorCommand},
     *     where <em>rrggbb</em> is the color as a 6-digit hex string</li>
     * </ul>
     *
     * @param symbol the symbol representing the character which executes the command
     * @param action a string representing the command
     * @return {@code this}
     * @throws NullPointerException if {@code action} is {@code null}
     */
    @Override
    public LSystemBuilder registerCommand(char symbol, String action) {
        Objects.requireNonNull(action, "The action string must not be null.");

        this.commands.put(symbol, parseCommand(action));
        return this;
    }

    /**
     * Configures the builder from an array of directives.
     * <p>
     * Allowed directives are:
     * <ul>
     *     <li>{@code origin} <em>x</em> <em>y</em>: sets the origin (see {@code setOrigin})
     *     <li>{@code angle} <em>a</em>: sets the angle (see {@code setAngle})
     *     <li>{@code unitLength} <em>l</em>: sets the unit length (see {@code setUnitLength})
     *     <li>{@code unitLengthDegreeScaler} <em>s</em>: sets the unit length degree scaler
     *     (see {@code setUnitLengthDegreeScaler}); the scaler may be given as a fraction ({@code a / b}).
     *     <li>{@code command} <em>c</em>: registers a new command (see {@code registerCommand})
     *     <li>{@code axiom} <em>a</em>: sets the axiom (see {@code setAxiom})
     *     <li>{@code production} <em>p</em>: registers a new production (see {@code registerProduction})
     * </ul>
     * Empty directives are ignored.
     *
     * @param lines an array of directives
     * @return {@code this}
     * @throws NullPointerException if {@code lines} is {@code null}
     */
    @Override
    public LSystemBuilder configureFromText(String[] lines) {
        Objects.requireNonNull(lines, "The lines array must not be null.");

        for (String line : lines)
            parseAndApplyDirective(line);
        return this;
    }

    /**
     * Builds a new {@link LSystem L-system}.
     *
     * @return a new L-system
     */
    @Override
    public LSystem build() {
        return new LSystemImpl();
    }

    /**
     * Parses an action string into a command.
     *
     * @param action the string to parse
     * @return the resulting command
     * @see #registerCommand(char, String)
     */
    private static Command parseCommand(String action) {
        Scanner s = new Scanner(action);

        String commandName = s.next();
        return switch (commandName) {
            case "draw" -> new DrawCommand(s.nextDouble());
            case "skip" -> new SkipCommand(s.nextDouble());
            case "scale" -> new ScaleCommand(s.nextDouble());
            case "rotate" -> new RotateCommand(s.nextDouble());
            case "push" -> new PushCommand();
            case "pop" -> new PopCommand();
            case "color" -> new ColorCommand(new Color(s.nextInt(16)));
            default -> throw new IllegalArgumentException("Unknown command " + commandName);
        };
    }

    /**
     * Parses a directive string and applies it to the L-system builder.
     *
     * @param directive a directive to parse
     */
    private void parseAndApplyDirective(String directive) {
        Scanner s = new Scanner(directive);
        if (!s.hasNext())
            return;

        String directiveName = s.next();
        switch (directiveName) {
            case "origin" -> setOrigin(s.nextDouble(), s.nextDouble());
            case "angle" -> setAngle(s.nextDouble());
            case "unitLength" -> setUnitLength(s.nextDouble());
            case "unitLengthDegreeScaler" -> setUnitLengthDegreeScaler(parseFraction(restOfLine(s)));
            case "axiom" -> setAxiom(s.next());
            case "command" -> registerCommand(nextChar(s), restOfLine(s));
            case "production" -> registerProduction(nextChar(s), restOfLine(s));
            default -> throw new IllegalArgumentException("Unknown directive " + directiveName);
        }
    }

    /**
     * Finds the next non-whitespace character in a {@link Scanner}.
     *
     * @param s the scanner to use
     * @return the first non-whitespace character
     * @throws NoSuchElementException if no such character are available
     * @throws IllegalStateException  if {@code s} is closed
     */
    private static char nextChar(Scanner s) {
        return s.skip("\\s*").next("\\S").charAt(0);
    }

    /**
     * Skips consecutive whitespace and returns the rest of the line in a {@link Scanner}.
     *
     * @param s the scanner to use
     * @return the rest of the line without leading whitespace
     * @throws NoSuchElementException if no line was found
     * @throws IllegalStateException  if {@code s} is closed
     */
    private static String restOfLine(Scanner s) {
        return s.skip("\\s*").nextLine();
    }

    /**
     * Parses a string representing a fraction into a {@code double}.
     * <p>
     * The numerator and denominator are separated by a slash (solidus, '{@code /}') with optional whitespace around it.
     * If a single number was found, it is returned instead.
     *
     * @param str the string to parse
     * @return the fraction as a real number
     * @throws NullPointerException  if {@code str} is {@code null}
     * @throws NumberFormatException if {@code str} is not a valid fraction
     */
    private static double parseFraction(String str) {
        Objects.requireNonNull(str, "The fraction string must not be null");
        Pattern p = Pattern.compile("\\s*(\\d+(?:\\.\\d+)?)(?:\\s*/\\s*(\\d+(?:\\.\\d+)?))?");
        Matcher m = p.matcher(str);

        if (!m.matches())
            throw new NumberFormatException("The string " + str + " does not contain a fraction.");

        String numerator = m.group(1);
        String denominator = m.group(2);

        if (denominator == null)
            return Double.parseDouble(numerator);

        return Double.parseDouble(numerator) / Double.parseDouble(denominator);
    }

    /**
     * An implementation of an L-system.
     */
    private class LSystemImpl implements LSystem {
        /**
         * Generates a command string by consecutively applying the {@link LSystemBuilderImpl#productions productions}
         * to the {@link LSystemBuilderImpl#axiom}, {@code level} times.
         *
         * @param level the number of times to apply the productions to the axiom
         * @return the resulting command string
         * @throws IllegalArgumentException if {@code level} is negative
         */
        @Override
        public String generate(int level) {
            if (level < 0)
                throw new IllegalArgumentException("The level must not be negative, but " + level + " was passed.");

            if (level == 0)
                return axiom;

            return substitute(generate(level - 1));
        }

        /**
         * Applies the {@link LSystemBuilderImpl#productions productions} to a command string.
         *
         * @param commandString the command string to which to apply the productions
         * @return the resulting command string
         * @throws NullPointerException if {@code commandString} is {@code null}
         */
        private String substitute(String commandString) {
            Objects.requireNonNull(commandString, "The command string must not be null.");
            StringBuilder sb = new StringBuilder();

            for (char c : commandString.toCharArray()) {
                String production = productions.get(c);

                if (production == null)
                    sb.append(c);
                else
                    sb.append(production);
            }

            return sb.toString();
        }

        /**
         * Draws the given level of the L-system to a {@link Painter}.
         *
         * @param level   the number of times to apply the productions to the axiom before drawing
         * @param painter the painter onto which to draw
         * @see #generate(int)
         */
        @Override
        public void draw(int level, Painter painter) {
            Context ctx = new Context();
            TurtleState initialState = new TurtleState(origin, new Vector2D(1, 0).rotated(angle / 180 * Math.PI),
                    Color.BLACK, unitLength * Math.pow(unitLengthDegreeScaler, level));

            ctx.pushState(initialState);

            String commandString = generate(level);

            for (char c : commandString.toCharArray()) {
                Command command = commands.get(c);
                if (command != null)
                    commands.get(c).execute(ctx, painter);
            }
        }
    }
}
