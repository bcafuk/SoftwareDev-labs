package hr.fer.oprpp1.hw08.jnotepadpp.documentModel;

import java.nio.file.Path;

/**
 * Represents zero or more open documents, with one document designated as the current one.
 *
 * @author Borna Cafuk
 */
public interface MultipleDocumentModel extends Iterable<SingleDocumentModel> {
    /**
     * Adds a new, empty document to the model.
     *
     * @return the new document's model
     */
    SingleDocumentModel createNewDocument();

    /**
     * Gets the model of the current document.
     *
     * @return the model of the current document
     * @throws java.util.NoSuchElementException if the model contains no documents
     */
    SingleDocumentModel getCurrentDocument();

    /**
     * Loads a document from a path and adds it to the model.
     *
     * @param path the path from which to load the document
     * @return the new document's model
     * @throws NullPointerException if {@code path} is {@code null}
     */
    SingleDocumentModel loadDocument(Path path);

    /**
     * Saves one of the model's documents to disk.
     * <p>
     * If {@code newPath} is {@code null}, the document is saved to the path associated with it.
     * Otherwise, the document's path is changed and it is saved to the new path,
     * and all consecutive saves will also save to this path until it is changed again.
     *
     * @param model   the model of the document to save
     * @param newPath the document's new path, or {@code null}
     * @throws NullPointerException                      if {@code model} is {@code null}
     * @throws IllegalArgumentException                  if {@code newPath} is {@code null},
     *                                                   but the document does not have a path associated with itself
     * @throws java.util.ConcurrentModificationException if {@code newPath} is not {@code null},
     *                                                   but a file with that path is already open in the model
     */
    void saveDocument(SingleDocumentModel model, Path newPath);

    /**
     * Removes a document from the model.
     * <p>
     * Does not ask for confirmation if the document has been modified.
     * If such functionality is required, it should be handled before calling this method.
     *
     * @param model the model of the document to close
     * @throws NullPointerException if {@code model} is {@code null}
     */
    void closeDocument(SingleDocumentModel model);

    /**
     * Registers a {@link MultipleDocumentListener} which will be notified when a document is opened or closed,
     * or when which document is the current one changed.
     *
     * @param l the listener to register
     * @throws NullPointerException if {@code l} is {@code null}
     */
    void addMultipleDocumentListener(MultipleDocumentListener l);

    /**
     * Unregisters a listener from the model, if it is registered.
     *
     * @param l the listener to unregister
     */
    void removeMultipleDocumentListener(MultipleDocumentListener l);

    /**
     * Gets the number of currently open documents.
     *
     * @return the number of open documents
     */
    int getNumberOfDocuments();

    /**
     * Retrieves a document by its index.
     *
     * @param index the index of the document to get
     * @return the model of the {@code index}<sup>th</sup> document
     * @throws IndexOutOfBoundsException if {@code index} is negative,
     *                                   or greater than or equal to the number of open documents
     */
    SingleDocumentModel getDocument(int index);
}
