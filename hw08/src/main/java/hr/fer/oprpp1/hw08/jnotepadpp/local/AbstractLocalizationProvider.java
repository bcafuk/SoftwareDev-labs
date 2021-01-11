package hr.fer.oprpp1.hw08.jnotepadpp.local;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * An implementation of {@link ILocalizationProvider} which has an internal list used to
 * keep track of registered listeners.
 *
 * @author Borna Cafuk
 */
public abstract class AbstractLocalizationProvider implements ILocalizationProvider {
    /**
     * Listeners registered using {@link #addLocalizationListener(ILocalizationListener)}.
     */
    private final List<ILocalizationListener> listeners = new LinkedList<>();

    /**
     * Creates a new {@code AbstractLocalizationProvider}.
     */
    public AbstractLocalizationProvider() {}

    @Override
    public void addLocalizationListener(ILocalizationListener listener) {
        listeners.add(Objects.requireNonNull(listener, "The listener must not be null"));
    }

    @Override
    public void removeLocalizationListener(ILocalizationListener listener) {
        listeners.remove(listener);
    }

    /**
     * Notifies all registered listeners that the localization has changed.
     */
    public void fire() {
        for (ILocalizationListener listener : listeners)
            listener.localizationChanged();
    }
}
