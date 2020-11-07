package hr.fer.zemris.lsystems.impl;

import hr.fer.oprpp1.custom.collections.Dictionary;
import hr.fer.oprpp1.math.Vector2D;
import hr.fer.zemris.lsystems.LSystem;
import hr.fer.zemris.lsystems.LSystemBuilder;
import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.commands.*;

import java.awt.*;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LSystemBuilderImpl implements LSystemBuilder {
    private Dictionary<Character, String> productions = new Dictionary<>();
    private Dictionary<Character, Command> commands = new Dictionary<>();

    private double unitLength = 0.1;
    private double unitLengthDegreeScaler = 1;
    private Vector2D origin = new Vector2D(0, 0);
    private double angle = 0;
    private String axiom = "";

    @Override
    public LSystemBuilder setUnitLength(double unitLength) {
        this.unitLength = unitLength;
        return this;
    }

    @Override
    public LSystemBuilder setOrigin(double x, double y) {
        this.origin = new Vector2D(x, y);
        return this;
    }

    @Override
    public LSystemBuilder setAngle(double angle) {
        this.angle = angle;
        return this;
    }

    @Override
    public LSystemBuilder setAxiom(String axiom) {
        this.axiom = axiom;
        return this;
    }

    @Override
    public LSystemBuilder setUnitLengthDegreeScaler(double unitLengthDegreeScaler) {
        this.unitLengthDegreeScaler = unitLengthDegreeScaler;
        return this;
    }

    @Override
    public LSystemBuilder registerProduction(char symbol, String production) {
        Objects.requireNonNull(production, "The production string must not be null.");
        if (this.productions.get(symbol) != null)
            throw new IllegalArgumentException("Tried to register action \"" + production + "\" for symbol '" + symbol + "', but action \"" + this.productions.get(symbol) + "\" is already register for it.");

        this.productions.put(symbol, production);
        return this;
    }

    @Override
    public LSystemBuilder registerCommand(char symbol, String action) {
        Objects.requireNonNull(action, "The action string must not be null.");
        if (this.commands.get(symbol) != null)
            throw new IllegalArgumentException("Tried to register action \"" + action + "\" for symbol '" + symbol + "', but an action is already registered for it.");

        this.commands.put(symbol, parseCommand(action));
        return this;
    }

    @Override
    public LSystemBuilder configureFromText(String[] lines) {
        Objects.requireNonNull(lines, "The lines array must not be null.");

        for (String line : lines)
            parseAndApplyDirective(line);
        return this;
    }

    @Override
    public LSystem build() {
        return new LSystemImpl();
    }

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

    private static char nextChar(Scanner s) {
        return s.skip("\\s*").next("\\S").charAt(0);
    }

    private static String restOfLine(Scanner s) {
        return s.skip("\\s*").nextLine();
    }

    private static double parseFraction(String str) {
        Pattern p = Pattern.compile("\\s*(\\d+(?:\\.\\d+)?)(?:\\s*/\\s*(\\d+(?:\\.\\d+)?))?");
        Matcher m = p.matcher(str);

        if (!m.matches())
            throw new IllegalArgumentException("The string " + str + " does not contain a fraction.");

        String numerator = m.group(1);
        String denominator = m.group(2);

        if (denominator == null)
            return Double.parseDouble(numerator);

        return Double.parseDouble(numerator) / Double.parseDouble(denominator);
    }

    private class LSystemImpl implements LSystem {
        @Override
        public String generate(int level) {
            if (level == 0)
                return axiom;

            return substitute(generate(level - 1));
        }

        private String substitute(String s) {
            StringBuilder sb = new StringBuilder();

            for (char c : s.toCharArray()) {
                String production = productions.get(c);

                if (production == null)
                    sb.append(c);
                else
                    sb.append(production);
            }

            return sb.toString();
        }

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
