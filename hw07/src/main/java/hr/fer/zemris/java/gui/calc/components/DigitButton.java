package hr.fer.zemris.java.gui.calc.components;

import hr.fer.zemris.java.gui.calc.model.CalcModel;
import hr.fer.zemris.java.gui.calc.model.CalculatorInputException;

import java.util.Objects;

/**
 * A button used to input a digit into a calculator.
 *
 * @author Borna Cafuk
 */
public class DigitButton extends CalculatorButton {
    public static final float FONT_SIZE = 30.0f;

    /**
     * Constructs a new digit button.
     *
     * @param model the model to which to add the digit
     * @param digit the digit to add to the model
     * @throws NullPointerException     if {@code model} is {@code null}
     * @throws IllegalArgumentException if {@code digit} < 0 or
     */
    public DigitButton(CalcModel model, int digit) {
        super(Integer.toString(digit));

        Objects.requireNonNull(model, "The model must not be null");
        if (digit < 0 || digit > 9)
            throw new IllegalArgumentException("The digit must be in the range [0, 9], but was " + digit);

        setFont(getFont().deriveFont(FONT_SIZE));

        addActionListener(e -> model.insertDigit(digit));
    }
}
