package hr.fer.zemris.java.gui.calc.components;

import hr.fer.zemris.java.gui.calc.model.CalcModel;

import java.util.Objects;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleUnaryOperator;

/**
 * A button used to input a unary operation.
 *
 * @author Borna Cafuk
 */
public class UnaryOpButton extends CalculatorButton {
    /**
     * Constructs a button for a non-invertible unary operator.
     *
     * @param model    the model with which to calculate
     * @param text     the text of the button
     * @param operator the operator
     * @throws NullPointerException if {@code model} or {@code operator} is {@code null}
     */
    public UnaryOpButton(CalcModel model, String text, DoubleUnaryOperator operator) {
        this(model, text, operator, operator, () -> false);
    }

    /**
     * Constructs a button for an invertible unary operator.
     *
     * @param model      the model with which to calculate
     * @param text       the text of the button
     * @param regularOp  the regular, non-inverted operator
     * @param inverseOp  the inverted operator
     * @param isInverted a supplier used to choose between the operators;
     *                   when it returns {@code true}, {@code inverseOp} is used, otherwise {@code regularOp} is used
     * @throws NullPointerException if {@code model}, {@code regularOp}, {@code inverseOp},
     *                              or {@code isInverted} is {@code null}
     */
    public UnaryOpButton(CalcModel model, String text,
                         DoubleUnaryOperator regularOp, DoubleUnaryOperator inverseOp, BooleanSupplier isInverted) {
        super(text);

        Objects.requireNonNull(model, "The model must not be null");
        Objects.requireNonNull(regularOp, "The regular operator must not be null");
        Objects.requireNonNull(inverseOp, "The inverse operator must not be null");
        Objects.requireNonNull(isInverted, "The isInverted supplier must not be null");

        addActionListener(e -> {
            DoubleUnaryOperator operator = isInverted.getAsBoolean() ? inverseOp : regularOp;
            model.setValue(operator.applyAsDouble(model.getValue()));
        });
    }
}
