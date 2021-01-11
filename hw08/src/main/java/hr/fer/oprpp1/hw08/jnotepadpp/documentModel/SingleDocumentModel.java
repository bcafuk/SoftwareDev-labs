package hr.fer.oprpp1.hw08.jnotepadpp.documentModel;

import javax.swing.*;
import java.nio.file.Path;

/**
 * Represents an editable text document.
 *
 * @author Borna Cafuk
 */
public interface SingleDocumentModel {
    /**
     * Gets the component holding the contents of the document.
     *
     * @return the {@link JTextArea} with the contents
     */
    JTextArea getTextComponent();

    /**
     * Returns the path associated with the document.
     *
     * @return the path of the file, or {@code null} if there is none associated
     */
    Path getFilePath();

    /**
     * Associates a path with the document.
     *
     * @param path the new path of the file, or {@code null} if the document should no longer have a path associated
     */
    void setFilePath(Path path);

    /**
     * Checks the file's modification flag.
     *
     * @return the file's modification flag
     */
    boolean isModified();

    /**
     * Sets the file's modification flag.
     *
     * @param modified the new value for the file's modification flag
     */
    void setModified(boolean modified);

    /**
     * Registers a {@link SingleDocumentListener} which will be notified when the document's modification flag changes
     * state, or when the file's path is changed.
     *
     * @param l the listener to register
     * @throws NullPointerException if {@code l} is {@code null}
     */
    void addSingleDocumentListener(SingleDocumentListener l);

    /**
     * Unregisters a listener from the model, if it is registered.
     *
     * @param l the listener to unregister
     */
    void removeSingleDocumentListener(SingleDocumentListener l);
}
