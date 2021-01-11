package hr.fer.oprpp1.hw08.jnotepadpp.documentModel;

/**
 * Listens for changes in a {@link SingleDocumentListener}.
 *
 * @author Borna Cafuk
 */
public interface SingleDocumentListener {
    /**
     * Called when the document's modification flag changes state.
     *
     * @param model the model whose flag changed state
     */
    void documentModifyStatusUpdated(SingleDocumentModel model);

    /**
     * Called when the document's path changes.
     *
     * @param model the model whose path changed
     */
    void documentFilePathUpdated(SingleDocumentModel model);
}
