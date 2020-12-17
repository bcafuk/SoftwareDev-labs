package hr.fer.zemris.java.gui.calc;

import hr.fer.zemris.java.gui.calc.components.BinaryOpButton;
import hr.fer.zemris.java.gui.calc.components.CalculatorButton;
import hr.fer.zemris.java.gui.calc.components.DigitButton;
import hr.fer.zemris.java.gui.calc.components.DisplayLabel;
import hr.fer.zemris.java.gui.calc.model.CalcModel;
import hr.fer.zemris.java.gui.layouts.CalcLayout;
import hr.fer.zemris.java.gui.layouts.RCPosition;

import javax.swing.*;
import java.awt.*;
import java.io.Serial;
import java.util.Stack;
import java.util.function.DoubleBinaryOperator;

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

        initOtherButtons();
        initNumpad();
        initOperatorButtons();
    }

    /**
     * Adds the non-digit, non-operator buttons to the calculator.
     */
    private void initOtherButtons() {
        Container cp = getContentPane();

        JButton equalsButton = new CalculatorButton("=");
        equalsButton.addActionListener(e -> {
            DoubleBinaryOperator pendingOp = model.getPendingBinaryOperation();

            if (pendingOp != null && model.isActiveOperandSet())
                model.setValue(pendingOp.applyAsDouble(model.getActiveOperand(), model.getValue()));
            else
                model.setValue(model.getValue());

            model.clearActiveOperand();
            model.setPendingBinaryOperation(null);
        });
        cp.add(equalsButton, new RCPosition(1, 6));

        JButton clearButton = new CalculatorButton("clr");
        clearButton.addActionListener(e -> model.clear());
        cp.add(clearButton, new RCPosition(1, 7));

        JButton resetButton = new CalculatorButton("reset");
        resetButton.addActionListener(e -> model.clearAll());
        cp.add(resetButton, new RCPosition(2, 7));

        // TODO: Push & pop
    }

    /**
     * Adds the numpad to the calculator.
     */
    private void initNumpad() {
        Container cp = getContentPane();

        cp.add(new DigitButton(model, 0), new RCPosition(5, 3));
        cp.add(new DigitButton(model, 1), new RCPosition(4, 3));
        cp.add(new DigitButton(model, 2), new RCPosition(4, 4));
        cp.add(new DigitButton(model, 3), new RCPosition(4, 5));
        cp.add(new DigitButton(model, 4), new RCPosition(3, 3));
        cp.add(new DigitButton(model, 5), new RCPosition(3, 4));
        cp.add(new DigitButton(model, 6), new RCPosition(3, 5));
        cp.add(new DigitButton(model, 7), new RCPosition(2, 3));
        cp.add(new DigitButton(model, 8), new RCPosition(2, 4));
        cp.add(new DigitButton(model, 9), new RCPosition(2, 5));

        JButton signButton = new CalculatorButton("+/-");
        signButton.addActionListener(e -> {
            if (model.isEditable())
                model.swapSign();
        });
        cp.add(signButton, new RCPosition(5, 4));

        JButton decimalButton = new CalculatorButton(".");
        decimalButton.addActionListener(e -> model.insertDecimalPoint());
        cp.add(decimalButton, new RCPosition(5, 5));
    }

    /**
     * Adds the operator buttons to the calculator, as well as the inverting checkbox.
     */
    private void initOperatorButtons() {
        Container cp = getContentPane();

        JCheckBox invertOperations = new JCheckBox("Inv");
        cp.add(invertOperations, new RCPosition(5, 7));

        cp.add(new BinaryOpButton(model, "/", (a, b) -> a / b), new RCPosition(2, 6));
        cp.add(new BinaryOpButton(model, "*", (a, b) -> a * b), new RCPosition(3, 6));
        cp.add(new BinaryOpButton(model, "-", (a, b) -> a - b), new RCPosition(4, 6));
        cp.add(new BinaryOpButton(model, "+", (a, b) -> a + b), new RCPosition(5, 6));

        cp.add(new BinaryOpButton(model, "x ^ n",
                        Math::pow, (a, b) -> Math.pow(a, 1.0 / b), invertOperations::isSelected),
                new RCPosition(5, 1));

        // TODO: Unary operators
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
