package hr.fer.oprpp1.hw08.jnotepadpp.local.swing;

import hr.fer.oprpp1.hw08.jnotepadpp.local.ILocalizationProvider;
import hr.fer.oprpp1.hw08.jnotepadpp.local.LocalizationProviderBridge;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * A {@link LocalizationProviderBridge} linked to a {@link JFrame}.
 * Connects when the window is opened, and disconnects when it is closed.
 *
 * @author Borna Cafuk
 */
public class FrameLocalizationProvider extends LocalizationProviderBridge {
    /**
     * Creates a frame localization provider for a given provider and frame.
     *
     * @param parent the decorated provider
     * @param frame the frame for which to
     */
    public FrameLocalizationProvider(ILocalizationProvider parent, JFrame frame) {
        super(parent);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                connect();
            }

            @Override
            public void windowClosed(WindowEvent e) {
                disconnect();
            }
        });
    }
}
