package hr.fer.oprpp1.hw08.jnotepadpp.local;

import java.util.NoSuchElementException;

/**
 * Used to retrieve the localized versions of strings by their localization key.
 *
 * @author Borna Cafuk
 */
public interface ILocalizationProvider {
    /**
     * Gets a localized string.
     *
     * @param key the string's localization key
     * @return the localized string
     * @throws NullPointerException   if {@code key} is {@code null}
     * @throws NoSuchElementException if no translation string exists for the given key
     */
    String getString(String key);

    /**
     * Registers a listener to the provider.
     * <p>
     * The listener's {@link ILocalizationListener#localizationChanged()} method will be called
     * when the provider's localization strings change.
     *
     * @param listener the listener to register
     * @throws NullPointerException if {@code listener} is {@code null}
     */
    void addLocalizationListener(ILocalizationListener listener);

    /**
     * Unregisters a listener from the provider if it is registered.
     * <p>
     * If the listener is not registered, nothing is done.
     *
     * @param listener the listener to unregister
     */
    void removeLocalizationListener(ILocalizationListener listener);
}
