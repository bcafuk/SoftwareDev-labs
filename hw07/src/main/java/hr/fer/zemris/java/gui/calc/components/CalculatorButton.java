package hr.fer.zemris.java.gui.calc.components;

import javax.swing.*;
import java.awt.*;

/**
 * A JButton with the appropriate background color.
 *
 * @author Borna Cafuk
 */
public class CalculatorButton extends JButton {
    private static final Color BACKGROUND_COLOR = new Color(221, 221, 255);

    /**
     * Creates a button with text.
     *
     * @param text the text of the button
     */
    public CalculatorButton(String text) {
        super(text);

        setBackground(BACKGROUND_COLOR);
        setOpaque(true);
    }
}
