package hr.fer.zemris.java.gui.calc;

import hr.fer.zemris.java.gui.calc.model.CalcModel;
import hr.fer.zemris.java.gui.layouts.CalcLayout;

import javax.swing.*;
import java.awt.*;
import java.io.Serial;
import java.util.Stack;

/**
 * A calculator window.
 *
 * @author Borna Cafuk
 */
public class Calculator extends JFrame {
    /**
     * The size of the font in components where it is made larger
     */
    public static final float LARGE_FONT = 30.0f;

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * The model used to give the calculator functionality.
     */
    private final CalcModel model = new CalcModelImpl();
    /**
     * The stack used to memorize operands.
     */
    private final Stack<Double> stack = new Stack<>();

    /**
     * Constructs a new window.
     */
    public Calculator() {
        setSize(613, 266);
        // TODO: See if pack() will work
        setTitle("Java Calculator v1.0");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        initGUI();
    }

    /**
     * Initializes the GUI by setting the layout and adding the display and all buttons to it.
     */
    private void initGUI() {
        Container cp = getContentPane();
        cp.setLayout(new CalcLayout(5));

        // TODO: Initialize GUI
    }

    /**
     * Creates a new {@code Calculator} window and shows it.
     *
     * @param args unused
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new Calculator();
            frame.setVisible(true);
        });
    }
}
