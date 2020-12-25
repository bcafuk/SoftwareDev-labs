package hr.fer.oprpp1.hw08.jnotepadpp;

import hr.fer.oprpp1.hw08.jnotepadpp.documentModel.SingleDocumentListener;
import hr.fer.oprpp1.hw08.jnotepadpp.documentModel.SingleDocumentModel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * An implementation of the {@link SingleDocumentListener}.
 *
 * @author Borna Cafuk
 */
public class DefaultSingleDocumentModel implements SingleDocumentModel {
    /**
     * The path associated with the document, or {@code null} if none is associated.
     */
    private Path filePath;
    /**
     * The component holding the editable text.
     */
    private final JTextArea textComponent;

    /**
     * The modification flag.
     */
    private boolean isModified;

    /**
     * A list of registered listeners.
     */
    private final List<SingleDocumentListener> listeners = new LinkedList<>();

    /**
     * Constructs a document model with the given text content.
     *
     * @param path    the path to associate with the model, or {@code null}
     * @param content the initial content of the document
     * @throws NullPointerException if {@code content} is {@code null}
     */
    public DefaultSingleDocumentModel(Path path, String content) {
        filePath = path;
        textComponent = new JTextArea(Objects.requireNonNull(content, "The text content must not be null"));
        isModified = path == null;

        textComponent.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                setModified(true);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                setModified(true);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                setModified(true);
            }
        });
    }

    @Override
    public JTextArea getTextComponent() {
        return textComponent;
    }

    @Override
    public Path getFilePath() {
        return filePath;
    }

    @Override
    public void setFilePath(Path path) {
        filePath = path;

        for (SingleDocumentListener listener : listeners)
            listener.documentFilePathUpdated(this);
    }

    @Override
    public boolean isModified() {
        return isModified;
    }

    @Override
    public void setModified(boolean modified) {
        isModified = modified;

        for (SingleDocumentListener listener : listeners)
            listener.documentModifyStatusUpdated(this);
    }

    @Override
    public void addSingleDocumentListener(SingleDocumentListener l) {
        listeners.add(Objects.requireNonNull(l, "The listener must not be null"));
    }

    @Override
    public void removeSingleDocumentListener(SingleDocumentListener l) {
        listeners.remove(l);
    }
}
