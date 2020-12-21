package hr.fer.zemris.java.gui.calc.components;

import hr.fer.zemris.java.gui.calc.model.CalcModel;
import hr.fer.zemris.java.gui.calc.model.CalculatorInputException;

import java.util.Objects;
import java.util.function.DoubleUnaryOperator;

/**
 * A button used to input a unary operation.
 *
 * @author Borna Cafuk
 */
public class UnaryOpButton extends InvertibleOpButton {
    /**
     * Constructs a button for a non-invertible unary operator.
     * <p>
     * Creates a button which performs the same operation whether it is inverted or not.
     *
     * @param model    the model with which to calculate
     * @param text     the text of the button
     * @param operator the operator
     * @throws NullPointerException if {@code model} or {@code operator} is {@code null}
     */
    public UnaryOpButton(CalcModel model, String text, DoubleUnaryOperator operator) {
        this(model, text, text, operator, operator);
    }

    /**
     * Constructs a button for an invertible unary operator.
     *
     * @param model       the model with which to calculate
     * @param regularText the text of the button when it is not inverted
     * @param inverseText the text of the button when it is inverted
     * @param regularOp   the regular, non-inverted operator
     * @param inverseOp   the inverted operator
     * @throws NullPointerException if {@code model}, {@code regularOp}, or {@code inverseOp} is {@code null}
     */
    public UnaryOpButton(CalcModel model, String regularText, String inverseText,
                         DoubleUnaryOperator regularOp, DoubleUnaryOperator inverseOp) {
        super(regularText, inverseText);

        Objects.requireNonNull(model, "The model must not be null");
        Objects.requireNonNull(regularOp, "The regular operator must not be null");
        Objects.requireNonNull(inverseOp, "The inverse operator must not be null");

        addActionListener(e -> {
            if (model.hasFrozenValue())
                throw new CalculatorInputException("The calculator has a frozen value");

            DoubleUnaryOperator operator = isInverted() ? inverseOp : regularOp;
            model.setValue(operator.applyAsDouble(model.getValue()));
        });
    }
}
