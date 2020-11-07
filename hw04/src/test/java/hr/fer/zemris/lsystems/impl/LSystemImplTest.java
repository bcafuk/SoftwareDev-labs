package hr.fer.zemris.lsystems.impl;

import hr.fer.zemris.lsystems.LSystem;
import hr.fer.zemris.lsystems.LSystemBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LSystemImplTest {
    @Test
    public void testGenerate() {
        LSystemBuilder builder = new LSystemBuilderImpl();
        builder.setAxiom("F");
        builder.registerProduction('F', "F+F--F+F");

        LSystem system = builder.build();

        assertEquals("F", system.generate(0));
        assertEquals("F+F--F+F", system.generate(1));
        assertEquals("F+F--F+F+F+F--F+F--F+F--F+F+F+F--F+F", system.generate(2));
    }
}
