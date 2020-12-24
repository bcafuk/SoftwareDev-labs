package hr.fer.oprpp1.hw08.jnotepadpp.local;

import java.util.Objects;

/**
 * A decorator localization provider. Delegates all {@link #getString(String)} calls to the decorated provider.
 * Supports disconnecting from the decorated provider, in order to help garbage collection.
 * <p>
 * The motivation for this class arises from the fact that if a class uses a listener registered to
 * a global localization provider (like the singleton {@link LocalizationProvider}), the object using it
 * might not be able to be garbage collected because the global provider holds a reference to
 * the listener, which in turn might hold references to the instance using it or its members.
 * This class provides a way to easily connect and disconnect any number of listeners from a provider at once.
 *
 * @author Borna Cafuk
 */
public class LocalizationProviderBridge extends AbstractLocalizationProvider {
    /**
     * The provider to which all requests are delegated.
     */
    private final ILocalizationProvider parent;
    /**
     * The listener registered to {@link #parent}.
     */
    private final ILocalizationListener listener = this::fire;
    /**
     * Whether {@link #listener} is registered to {@link #parent}.
     */
    private boolean connected = false;

    /**
     * Constructs a new decorator for a localization provider.
     *
     * @param parent the provider for which to create a decorator
     * @throws NullPointerException if {@code parent} is {@code null}
     */
    public LocalizationProviderBridge(ILocalizationProvider parent) {
        this.parent = Objects.requireNonNull(parent, "The parent must not be null");
    }

    /**
     * Unregisters itself from the decorated provider.
     */
    public void disconnect() {
        this.connected = false;
        parent.removeLocalizationListener(listener);
    }

    /**
     * Registers itself to the decorated provider, if not already registered.
     */
    public void connect() {
        if (connected)
            return;

        connected = true;
        parent.addLocalizationListener(listener);
    }

    @Override
    public String getString(String key) {
        return parent.getString(key);
    }
}
