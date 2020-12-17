package hr.fer.zemris.java.gui.calc;

import hr.fer.zemris.java.gui.calc.components.CalculatorButton;
import hr.fer.zemris.java.gui.calc.components.DisplayLabel;
import hr.fer.zemris.java.gui.calc.model.CalcModel;
import hr.fer.zemris.java.gui.layouts.CalcLayout;
import hr.fer.zemris.java.gui.layouts.RCPosition;

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
        setSize(630, 305);
        setTitle("Java Calculator v1.0");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        initGUI();
        // TODO: See if pack() will work
    }

    /**
     * Initializes the GUI by setting the layout and adding the display and all buttons to it.
     */
    private void initGUI() {
        Container cp = getContentPane();
        cp.setLayout(new CalcLayout(5));

        JLabel display = new DisplayLabel(model);
        cp.add(display, new RCPosition(1, 1));

        JButton clearButton = new CalculatorButton("clr");
        clearButton.addActionListener(e -> model.clear());
        cp.add(clearButton, new RCPosition(1, 7));

        JButton equalsButton = new CalculatorButton("reset");
        clearButton.addActionListener(e -> model.clearAll());
        cp.add(equalsButton, new RCPosition(2, 7));

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
