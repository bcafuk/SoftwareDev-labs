package hr.fer.oprpp1.hw08.jnotepadpp.local.swing;

import hr.fer.oprpp1.hw08.jnotepadpp.local.ILocalizationListener;
import hr.fer.oprpp1.hw08.jnotepadpp.local.ILocalizationProvider;

import javax.swing.*;
import java.io.Serial;
import java.util.Objects;

/**
 * A {@link JMenu} with localized text.
 *
 * @author Borna Cafuk
 */
public class LJMenu extends JMenu {
    @Serial
    private static final long serialVersionUID = 1;

    /**
     * Creates a menu which displays the text from the given provider using the given key.
     *
     * @param key  the translation key used to get the displayed text
     * @param prov the provider used to get the displayed text
     * @throws NullPointerException if {@code key} or {@code prov} is {@code null}
     */
    public LJMenu(String key, ILocalizationProvider prov) {
        Objects.requireNonNull(key, "The localization key must not be null");
        Objects.requireNonNull(prov, "The localization provider must not be null");

        ILocalizationListener listener = () -> setText(prov.getString(key));

        listener.localizationChanged();
        prov.addLocalizationListener(listener);
    }
}
