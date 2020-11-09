package hr.fer.zemris.lsystems.demo;

import hr.fer.zemris.lsystems.LSystem;
import hr.fer.zemris.lsystems.LSystemBuilderProvider;
import hr.fer.zemris.lsystems.gui.LSystemViewer;
import hr.fer.zemris.lsystems.impl.LSystemBuilderImpl;

/**
 * A class demonstrating how to configure an {@link LSystemBuilderImpl} using methods.
 * <p>
 * The contents of these methods have been copied verbatim from
 * the assignment document written by doc. dr. sc. Marko Čupić.
 */
public class Glavni1 {
    /**
     * The contents of this method have been copied verbatim from
     * the assignment document written by doc. dr. sc. Marko Čupić.
     */
    private static LSystem createKochCurve(LSystemBuilderProvider provider) {
        return provider.createLSystemBuilder()
                       .registerCommand('F', "draw 1")
                       .registerCommand('+', "rotate 60")
                       .registerCommand('-', "rotate -60")
                       .setOrigin(0.05, 0.4)
                       .setAngle(0)
                       .setUnitLength(0.9)
                       .setUnitLengthDegreeScaler(1.0 / 3.0)
                       .registerProduction('F', "F+F--F+F")
                       .setAxiom("F")
                       .build();
    }

    public static void main(String[] args) {
        LSystemViewer.showLSystem(createKochCurve(LSystemBuilderImpl::new));
    }
}
