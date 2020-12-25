package hr.fer.oprpp1.hw08.jnotepadpp;

import hr.fer.oprpp1.hw08.jnotepadpp.documentModel.MultipleDocumentListener;
import hr.fer.oprpp1.hw08.jnotepadpp.documentModel.MultipleDocumentModel;
import hr.fer.oprpp1.hw08.jnotepadpp.documentModel.SingleDocumentListener;
import hr.fer.oprpp1.hw08.jnotepadpp.documentModel.SingleDocumentModel;
import hr.fer.oprpp1.hw08.jnotepadpp.local.ILocalizationProvider;

import javax.swing.*;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class DefaultMultipleDocumentModel extends JTabbedPane implements MultipleDocumentModel {
    /**
     * The icon displayed on tabs containing unsaved documents.
     */
    private static final ImageIcon unsavedIcon = Util.loadIcon("icons/unsaved.png");
    /**
     * The icon displayed on tabs containing saved documents.
     */
    private static final ImageIcon savedIcon = Util.loadIcon("icons/saved.png");

    /**
     * A list of open documents.
     */
    private final List<SingleDocumentModel> documents = new ArrayList<>();
    /**
     * The currently open document.
     */
    private SingleDocumentModel current = null;

    /**
     * A list of registered listeners.
     */
    private final List<MultipleDocumentListener> listeners = new LinkedList<>();

    /**
     * The localization provider used to fetch the text for unnamed documents.
     */
    private final ILocalizationProvider localizationProvider;

    /**
     * Constructs a new model.
     *
     * @param localizationProvider the localization provider used to fetch the text for unnamed documents
     */
    public DefaultMultipleDocumentModel(ILocalizationProvider localizationProvider) {
        this.localizationProvider = Objects.requireNonNull(localizationProvider,
                "The localization provider must not be null");

        localizationProvider.addLocalizationListener(() -> {
            for (int i = 0; i < getTabCount(); i++)
                updateTabNameAndTooltip(i);
        });
        addChangeListener(e -> notifyDocumentChanged());
    }

    @Override
    public SingleDocumentModel createNewDocument() {
        SingleDocumentModel document = new DefaultSingleDocumentModel(null, "");
        addDocument(document);
        return document;
    }

    @Override
    public SingleDocumentModel getCurrentDocument() {
        if (current == null)
            throw new NoSuchElementException("The model contains no elements");

        return current;
    }

    @Override
    public SingleDocumentModel loadDocument(Path path) {
        Objects.requireNonNull(path, "The path must not be null");

        for (int i = 0; i < documents.size(); i++) {
            SingleDocumentModel document = documents.get(i);
            if (path.equals(document.getFilePath())) {
                setSelectedIndex(i);
                return document;
            }
        }

        String content;
        try {
            content = Files.readString(path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        SingleDocumentModel document = new DefaultSingleDocumentModel(path, content);
        addDocument(document);
        return document;
    }

    @Override
    public void saveDocument(SingleDocumentModel model, Path newPath) {

    }

    @Override
    public void closeDocument(SingleDocumentModel model) {
        int index = documents.indexOf(model);

        documents.remove(index);
        removeTabAt(index);

        for (MultipleDocumentListener listener : listeners)
            listener.documentRemoved(model);
    }

    @Override
    public void addMultipleDocumentListener(MultipleDocumentListener l) {
        listeners.add(Objects.requireNonNull(l, "The listener must not be null"));
    }

    @Override
    public void removeMultipleDocumentListener(MultipleDocumentListener l) {
        listeners.remove(l);
    }

    @Override
    public int getNumberOfDocuments() {
        return documents.size();
    }

    @Override
    public SingleDocumentModel getDocument(int index) {
        return documents.get(index);
    }

    @Override
    public Iterator<SingleDocumentModel> iterator() {
        return documents.iterator();
    }

    /**
     * Adds a new document to the model and selects it.
     *
     * @param model the model of the document to add
     */
    private void addDocument(SingleDocumentModel model) {
        JScrollPane scrollPane = new JScrollPane(model.getTextComponent());
        documents.add(model);

        addTab(null, null, scrollPane);
        updateTabIcon(documents.size() - 1);
        updateTabNameAndTooltip(documents.size() - 1);

        model.addSingleDocumentListener(new SingleDocumentListener() {
            @Override
            public void documentModifyStatusUpdated(SingleDocumentModel model) {
                updateTabIcon(documents.indexOf(model));
            }

            @Override
            public void documentFilePathUpdated(SingleDocumentModel model) {
                updateTabNameAndTooltip(documents.indexOf(model));
            }
        });

        for (MultipleDocumentListener listener : listeners)
            listener.documentAdded(model);

        setSelectedIndex(documents.size() - 1);
    }

    /**
     * Notifies all registered listeners that the current document has changed.
     */
    private void notifyDocumentChanged() {
        SingleDocumentModel previous = current;
        current = getSelectedIndex() != -1 ? getDocument(getSelectedIndex()) : null;

        for (MultipleDocumentListener listener : listeners)
            listener.currentDocumentChanged(previous, current);
    }

    /**
     * Updates the tab name and tooltip with information about the file path.
     *
     * @param index the index of the tab to update
     */
    private void updateTabNameAndTooltip(int index) {
        SingleDocumentModel model = documents.get(index);

        String tabTitle = model.getFilePath() != null ?
                model.getFilePath().getFileName().toString() :
                localizationProvider.getString("document.unnamed");
        String tabTooltip = model.getFilePath() != null ?
                model.getFilePath().toAbsolutePath().toString() :
                localizationProvider.getString("document.unnamed");

        setTitleAt(index, tabTitle);
        setToolTipTextAt(index, tabTooltip);
    }

    /**
     * Updates the tab icon with the correct icon depending on the document's modification flag.
     *
     * @param index the index of the tab to update
     */
    private void updateTabIcon(int index) {
        SingleDocumentModel model = documents.get(index);
        setIconAt(index, model.isModified() ? unsavedIcon : savedIcon);
    }
}
