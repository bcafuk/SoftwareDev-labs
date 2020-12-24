package hr.fer.oprpp1.hw08.jnotepadpp.local;

import java.util.*;

/**
 * A singleton localization provider using translations from a resource bundle.
 * <p>
 * This behavior was specified in the assignment document.
 *
 * @author Borna Cafuk
 */
public class LocalizationProvider extends AbstractLocalizationProvider {
    /**
     * The base name of the bundle holding the translated strings.
     */
    private static final String BUNDLE_BASE_NAME = "hr.fer.oprpp1.hw08.jnotepadpp.local.translations";

    /**
     * The instance of the singleton.
     */
    private static final LocalizationProvider instance = new LocalizationProvider();

    /**
     * The locale of the provider.
     * <p>
     * Can be used to retrieve collators appropriate for the provider's current language.
     */
    private Locale locale = null;
    /**
     * The bundle holding the translated strings.
     */
    private ResourceBundle bundle = null;

    /**
     * Constructs a localization provider with no defined language.
     */
    private LocalizationProvider() {}

    /**
     * Retrieves the instance of the singleton.
     *
     * @return the instance of the localization provider singleton
     */
    public static LocalizationProvider getInstance() {
        return instance;
    }

    /**
     * Sets the provider's language.
     * <p>
     * Notifies all registered listeners of the change.
     *
     * @param language the new language of the provider; parsed using {@link Locale#forLanguageTag(String)}
     * @throws NullPointerException               if {@code language} is {@code null}
     * @throws java.util.MissingResourceException if no resource exists for the given language
     */
    public void setLanguage(String language) {
        locale = Locale.forLanguageTag(Objects.requireNonNull(language, "The language must not be null"));
        bundle = ResourceBundle.getBundle(BUNDLE_BASE_NAME, locale);

        fire();
    }

    /**
     * @throws NullPointerException   {@inheritDoc}
     * @throws NoSuchElementException {@inheritDoc}
     * @throws ClassCastException     if the resource associated with the given key is not a string
     */
    @Override
    public String getString(String key) {
        try {
            return bundle.getString(key);
        } catch (MissingResourceException e) {
            throw new NoSuchElementException("No translation string for key " + key, e);
        }
    }

    /**
     * Returns the provider's current locale.
     *
     * @return the provider's current locale
     */
    public Locale getLocale() {
        return locale;
    }
}
