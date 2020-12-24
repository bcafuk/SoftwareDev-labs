package hr.fer.oprpp1.hw08.jnotepadpp.local;

/**
 * A listener to be registered to a {@link ILocalizationProvider} and called by it when its localization changes.
 *
 * @author Borna Cafuk
 */
@FunctionalInterface
public interface ILocalizationListener {
    /**
     * The method called by the {@link ILocalizationProvider} when its localization changes.
     */
    void localizationChanged();
}
