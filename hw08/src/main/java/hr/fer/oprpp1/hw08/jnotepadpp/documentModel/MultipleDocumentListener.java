package hr.fer.oprpp1.hw08.jnotepadpp.documentModel;

/**
 * Listens for changes in a {@link MultipleDocumentModel}.
 *
 * @author Borna Cafuk
 */
public interface MultipleDocumentListener {
    /**
     * Called when a different document is made the current one.
     *
     * @param previousModel the model of the document which was previously the current one
     * @param currentModel  the model of the document which is now the current one
     */
    void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel);

    /**
     * Called when a new document is added to the {@link MultipleDocumentModel}.
     *
     * @param model the model of the new document
     */
    void documentAdded(SingleDocumentModel model);

    /**
     * Called when a document is removed from the {@link MultipleDocumentModel}.
     *
     * @param model the model of the closed document
     */
    void documentRemoved(SingleDocumentModel model);
}
