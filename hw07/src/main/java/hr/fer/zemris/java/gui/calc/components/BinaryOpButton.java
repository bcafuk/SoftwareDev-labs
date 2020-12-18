package hr.fer.zemris.java.gui.calc.components;

import hr.fer.zemris.java.gui.calc.Util;
import hr.fer.zemris.java.gui.calc.model.CalcModel;
import hr.fer.zemris.java.gui.calc.model.CalculatorInputException;

import java.util.Objects;
import java.util.function.DoubleBinaryOperator;

/**
 * A button used to input a binary operation.
 *
 * @author Borna Cafuk
 */
public class BinaryOpButton extends InvertibleOpButton {
    /**
     * Constructs a button for a non-invertible binary operator.
     * <p>
     * Creates a button which performs the same operation whether it is inverted or not.
     *
     * @param model    the model with which to calculate
     * @param text     the text of the button
     * @param operator the operator
     * @throws NullPointerException if {@code model} or {@code operator} is {@code null}
     */
    public BinaryOpButton(CalcModel model, String text, DoubleBinaryOperator operator) {
        this(model, text, text, operator, operator);
    }

    /**
     * Constructs a button for an invertible binary operator.
     *
     * @param model       the model with which to calculate
     * @param regularText the text of the button when it is not inverted
     * @param inverseText the text of the button when it is inverted
     * @param regularOp   the regular, non-inverted operator
     * @param inverseOp   the inverted operator
     * @throws NullPointerException if {@code model}, {@code regularOp}, or {@code inverseOp} is {@code null}
     */
    public BinaryOpButton(CalcModel model, String regularText, String inverseText,
                          DoubleBinaryOperator regularOp, DoubleBinaryOperator inverseOp) {
        super(regularText, inverseText);

        Objects.requireNonNull(model, "The model must not be null");
        Objects.requireNonNull(regularOp, "The regular operator must not be null");
        Objects.requireNonNull(inverseOp, "The inverse operator must not be null");

        addActionListener(e -> {
            if (model.hasFrozenValue())
                throw new CalculatorInputException("The calculator has a frozen value");

            DoubleBinaryOperator pendingOp = model.getPendingBinaryOperation();

            if (pendingOp != null && model.isActiveOperandSet())
                model.setActiveOperand(pendingOp.applyAsDouble(model.getActiveOperand(), model.getValue()));
            else
                model.setActiveOperand(model.getValue());

            model.freezeValue(Util.formatDouble(model.getActiveOperand()));
            model.setPendingBinaryOperation(isInverted() ? inverseOp : regularOp);
            model.clear();
        });
    }
}
