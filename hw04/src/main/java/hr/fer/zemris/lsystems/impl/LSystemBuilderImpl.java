package hr.fer.zemris.lsystems.impl;

import hr.fer.oprpp1.custom.collections.Dictionary;
import hr.fer.oprpp1.math.Vector2D;
import hr.fer.zemris.lsystems.LSystem;
import hr.fer.zemris.lsystems.LSystemBuilder;
import hr.fer.zemris.lsystems.Painter;

import java.util.Objects;

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
        // TODO: Implement method
        return null;
    }

    private static Command parseCommand(String action) {
        // TODO: Implement method
        return null;
    }

    private void parseAndApplyDirective(String directive) {
        // TODO: Implement method
    }

    private class LSystemImpl implements LSystem {
        @Override
        public String generate(int level) {
            // TODO: Implement method
            return null;
        }

        @Override
        public void draw(int level, Painter painter) {
            // TODO: Implement method
        }
    }
}
