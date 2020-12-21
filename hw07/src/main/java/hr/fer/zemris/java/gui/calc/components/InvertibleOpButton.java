package hr.fer.zemris.java.gui.calc.components;

/**
 * A button used to input an invertible operation.
 */
public abstract class InvertibleOpButton extends CalculatorButton {
    /**
     * The text of the button when not inverted.
     */
    private final String regularText;
    /**
     * The text of the button when inverted.
     */
    private final String inverseText;
    /**
     * Whether the button is inverted.
     */
    private boolean isInverted;

    /**
     * Creates a button with text.
     *
     * @param regularText the text of the button when not inverted
     * @param inverseText the text of the button when inverted
     */
    public InvertibleOpButton(String regularText, String inverseText) {
        super();

        this.regularText = regularText;
        this.inverseText = inverseText;
        setInverted(false);
    }

    /**
     * Returns whether the button currently is set to use the inverted or non-inverted operation.
     *
     * @return {@code true} if the inverse operation is to be used, {@code false} otherwise
     */
    public boolean isInverted() {
        return isInverted;
    }

    /**
     * Sets whether the button currently is set to use the inverted or non-inverted operation.
     *
     * @param inverted the button will be set to use the inverse operation if {@code true},
     *                 or to use the regular, non-inverted one if it is {@code false}
     */
    public void setInverted(boolean inverted) {
        isInverted = inverted;
        setText(isInverted ? inverseText : regularText);
    }
}
