package hr.fer.oprpp1.hw08.jnotepadpp;

import hr.fer.oprpp1.hw08.jnotepadpp.local.LocalizationProvider;
import hr.fer.oprpp1.hw08.jnotepadpp.local.swing.FrameLocalizationProvider;

import javax.swing.*;
import java.awt.*;
import java.io.Serial;

/**
 * The window of JNotepad++, a text editor which supports editing multiple files in tabs.
 *
 * @author Borna Cafuk
 */
public class JNotepadPP extends JFrame {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * The default language used at startup.
     */
    private static final String DEFAULT_LANGUAGE = "en";

    /**
     * The localization provider used to localize the applications' controls.
     */
    private final FrameLocalizationProvider localizationProvider;

    /**
     * Constructs a new JNotepad++ window.
     *
     * @throws HeadlessException if {@link GraphicsEnvironment#isHeadless()} returns {@code true}.
     */
    public JNotepadPP() throws HeadlessException {
        localizationProvider = new FrameLocalizationProvider(LocalizationProvider.getInstance(), this);

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("JNotepad++");
        initGUI();

        setSize(800, 600);
        setLocationRelativeTo(null);
    }

    /**
     * Fills the frame with the required GUI elements.
     */
    private void initGUI() {
    }

    /**
     * Creates a new JNotepad++ window.
     *
     * @param args unused
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LocalizationProvider.getInstance().setLanguage(DEFAULT_LANGUAGE);
            new JNotepadPP().setVisible(true);
        });
    }
}
