package hr.fer.oprpp1.hw08.jnotepadpp.local.swing;

import hr.fer.oprpp1.hw08.jnotepadpp.local.ILocalizationListener;
import hr.fer.oprpp1.hw08.jnotepadpp.local.ILocalizationProvider;

import javax.swing.*;
import java.io.Serial;
import java.util.Objects;

/**
 * An {@link Action} with a localized name.
 *
 * @author Borna Cafuk
 */
public abstract class LocalizableAction extends AbstractAction {
    @Serial
    private static final long serialVersionUID = 1;

    /**
     * Creates an action whose name is retrieved from the given provider using the given key.
     *
     * @param prov    the provider used to get the name of the action
     * @param nameKey the translation key used to get the name of the action
     * @throws NullPointerException if {@code prov} or {@code nameKey} is {@code null}
     */
    public LocalizableAction(ILocalizationProvider prov, String nameKey) {
        this(prov, nameKey, null);
    }

    /**
     * Creates an action whose name and short description are retrieved from the given provider using the given key.
     *
     * @param prov                the provider used to get the name and short of the action
     * @param nameKey             the translation key used to get the name of the action
     * @param shortDescriptionKey the translation key used to get the short description of the action,
     *                            or {@code null} if none should be set
     * @throws NullPointerException if {@code prov} or {@code nameKey} is {@code null}
     */
    public LocalizableAction(ILocalizationProvider prov, String nameKey, String shortDescriptionKey) {
        Objects.requireNonNull(nameKey, "The name localization key must not be null");
        Objects.requireNonNull(prov, "The localization provider must not be null");

        ILocalizationListener listener = () -> {
            putValue(NAME, prov.getString(nameKey));

            if (shortDescriptionKey != null)
                putValue(SHORT_DESCRIPTION, prov.getString(shortDescriptionKey));
        };

        listener.localizationChanged();
        prov.addLocalizationListener(listener);
    }
}
