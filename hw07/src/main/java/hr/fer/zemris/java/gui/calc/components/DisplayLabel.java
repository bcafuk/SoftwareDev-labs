package hr.fer.zemris.java.gui.calc.components;

import hr.fer.zemris.java.gui.calc.model.CalcModel;

import javax.swing.*;
import java.awt.*;

/**
 * A JLabel with a yellow background, and large, right-aligned text.
 * Responds automatically to {@link CalcModel} changes.
 *
 * @author Borna Cafuk
 */
public class DisplayLabel extends JLabel {
    public static final float FONT_SIZE = 30.0f;
    private static final Color BACKGROUND_COLOR = Color.YELLOW;

    /**
     * Creates a display which listens for updates from a calculator model.
     *
     * @param model the calculator model whose changes will be displayed by the label
     */
    public DisplayLabel(CalcModel model) {
        super("", RIGHT);
        setOpaque(true);
        setBackground(BACKGROUND_COLOR);
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        setFont(getFont().deriveFont(FONT_SIZE));

        setText(model.toString());
        model.addCalcValueListener(m -> SwingUtilities.invokeLater(() -> setText(m.toString())));
    }
}
