package hr.fer.oprpp1.hw08.jnotepadpp;

import hr.fer.oprpp1.hw08.jnotepadpp.documentModel.MultipleDocumentListener;
import hr.fer.oprpp1.hw08.jnotepadpp.documentModel.SingleDocumentListener;
import hr.fer.oprpp1.hw08.jnotepadpp.documentModel.SingleDocumentModel;
import hr.fer.oprpp1.hw08.jnotepadpp.local.LocalizationProvider;
import hr.fer.oprpp1.hw08.jnotepadpp.local.swing.FrameLocalizationProvider;
import hr.fer.oprpp1.hw08.jnotepadpp.local.swing.LJMenu;
import hr.fer.oprpp1.hw08.jnotepadpp.local.swing.LocalizableAction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.Serial;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

/**
 * The window of JNotepad++, a text editor which supports editing multiple files in tabs.
 *
 * @author Borna Cafuk
 */
public class JNotepadPP extends JFrame {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * The default language used at startup.
     */
    private static final String DEFAULT_LANGUAGE = "en";

    /**
     * The localization provider used to localize the applications' controls.
     */
    private final FrameLocalizationProvider localizationProvider;

    /**
     * The model and component holding the document tabs.
     */
    private DefaultMultipleDocumentModel documents;

    /**
     * The file chooser used when opening or saving files.
     */
    private final JFileChooser fileChooser = new JFileChooser();
    /**
     * The window's menu bar.
     */
    private JMenuBar menuBar;
    /**
     * A dockable toolbar.
     */
    private JToolBar toolBar;

    /**
     * A list of listeners to be attached to the currently active document.
     */
    private final List<SingleDocumentListener> documentListeners = new ArrayList<>();

    /**
     * Constructs a new JNotepad++ window.
     *
     * @throws HeadlessException if {@link GraphicsEnvironment#isHeadless()} returns {@code true}.
     */
    public JNotepadPP() throws HeadlessException {
        localizationProvider = new FrameLocalizationProvider(LocalizationProvider.getInstance(), this);

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        initGUI();

        setSize(800, 600);
        setLocationRelativeTo(null);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                tryExit();
            }
        });

        localizationProvider.addLocalizationListener(() -> fileChooser.setLocale(fileChooser.getLocale()));

    }

    /**
     * Fills the frame with the required GUI elements.
     */
    private void initGUI() {
        getContentPane().setLayout(new BorderLayout());

        documents = new DefaultMultipleDocumentModel(localizationProvider);
        getContentPane().add(documents, BorderLayout.CENTER);

        toolBar = new JToolBar();
        getContentPane().add(toolBar, BorderLayout.PAGE_START);

        menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        initFile();
        toolBar.addSeparator();
        initLanguage();

        updateTitle();

        documents.addMultipleDocumentListener(new MultipleDocumentListener() {
            @Override
            public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
                updateTitle();

                for (SingleDocumentListener listener : documentListeners) {
                    if (previousModel != null)
                        previousModel.removeSingleDocumentListener(listener);

                    if (currentModel != null)
                        currentModel.addSingleDocumentListener(listener);
                }
            }

            @Override
            public void documentAdded(SingleDocumentModel model) {}

            @Override
            public void documentRemoved(SingleDocumentModel model) {}
        });

        localizationProvider.addLocalizationListener(this::updateTitle);
    }

    /**
     * Initializes the <i>File</i> menu in the menu bar and its corresponding part of the toolbar.
     */
    private void initFile() {
        JMenu fileMenu = new LJMenu("file", localizationProvider);
        fileMenu.setMnemonic(KeyEvent.VK_F);
        menuBar.add(fileMenu);


        LocalizableAction newAction = new LocalizableAction(localizationProvider,
                "file.new", "file.new.description") {
            @Override
            public void actionPerformed(ActionEvent e) {
                documents.createNewDocument();
            }
        };
        newAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
        newAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_N);

        toolBar.add(newAction);
        fileMenu.add(newAction);


        LocalizableAction openAction = new LocalizableAction(localizationProvider,
                "file.open", "file.open.description") {
            @Override
            public void actionPerformed(ActionEvent e) {
                int chosenOption = fileChooser.showOpenDialog(JNotepadPP.this);

                if (chosenOption == JFileChooser.APPROVE_OPTION) {
                    Path path = (fileChooser.getSelectedFile()).toPath();

                    try {
                        documents.loadDocument(path);
                    } catch (UncheckedIOException ex) {
                        localizedMessageDialog(JOptionPane.ERROR_MESSAGE, "file.open.error");
                    }
                }
            }
        };
        openAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
        openAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_O);

        toolBar.add(openAction);
        fileMenu.add(openAction);


        LocalizableAction saveAction = new LocalizableAction(localizationProvider,
                "file.save", "file.save.description") {
            @Override
            public void actionPerformed(ActionEvent e) {
                trySave(documents.getCurrentDocument(), null);
            }
        };
        saveAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
        saveAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_S);
        saveAction.setEnabled(false);

        toolBar.add(saveAction);
        fileMenu.add(saveAction);


        LocalizableAction saveAsAction = new LocalizableAction(localizationProvider,
                "file.saveAs", "file.saveAs.description") {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveAs(documents.getCurrentDocument());
            }
        };
        saveAsAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
        saveAsAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_A);
        saveAsAction.setEnabled(false);

        toolBar.add(saveAsAction);
        fileMenu.add(saveAsAction);


        LocalizableAction closeAction = new LocalizableAction(localizationProvider,
                "file.close", "file.close.description") {
            @Override
            public void actionPerformed(ActionEvent e) {
                tryClose();
            }
        };
        closeAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_DOWN_MASK));
        closeAction.setEnabled(false);

        toolBar.add(closeAction);
        fileMenu.add(closeAction);


        fileMenu.addSeparator();


        LocalizableAction exitAction = new LocalizableAction(localizationProvider,
                "file.exit", "file.exit.description") {
            @Override
            public void actionPerformed(ActionEvent e) {
                tryExit();
            }
        };
        exitAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_DOWN_MASK));
        exitAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_X);

        toolBar.add(exitAction);
        fileMenu.add(exitAction);


        documentListeners.add(new SingleDocumentListener() {
            @Override
            public void documentModifyStatusUpdated(SingleDocumentModel model) {
                saveAction.setEnabled(model.isModified() && model.getFilePath() != null);
            }

            @Override
            public void documentFilePathUpdated(SingleDocumentModel model) {

            }
        });

        documents.addMultipleDocumentListener(new MultipleDocumentListener() {
            @Override
            public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
                if (currentModel != null) {
                    saveAction.setEnabled(currentModel.isModified() && currentModel.getFilePath() != null);
                }

                saveAsAction.setEnabled(documents.getNumberOfDocuments() > 0);
                closeAction.setEnabled(documents.getNumberOfDocuments() > 0);
            }

            @Override
            public void documentAdded(SingleDocumentModel model) {}

            @Override
            public void documentRemoved(SingleDocumentModel model) {}
        });
    }

    /**
     * Initializes the <i>Language</i> menu in the menu bar.
     */
    private void initLanguage() {
        JMenu languageMenu = new LJMenu("language", localizationProvider);
        languageMenu.setMnemonic(KeyEvent.VK_L);
        menuBar.add(languageMenu);


        LocalizableAction englishAction = new LocalizableAction(localizationProvider,
                "language.en") {
            @Override
            public void actionPerformed(ActionEvent e) {
                LocalizationProvider.getInstance().setLanguage("en");
            }
        };
        languageMenu.add(englishAction);


        LocalizableAction croatianAction = new LocalizableAction(localizationProvider,
                "language.hr") {
            @Override
            public void actionPerformed(ActionEvent e) {
                LocalizationProvider.getInstance().setLanguage("hr");
            }
        };
        languageMenu.add(croatianAction);


        LocalizableAction germanAction = new LocalizableAction(localizationProvider,
                "language.de") {
            @Override
            public void actionPerformed(ActionEvent e) {
                LocalizationProvider.getInstance().setLanguage("de");
            }
        };
        languageMenu.add(germanAction);
    }

    /**
     * Opens a dialog to choose the save location and attempts to save the file at that location.
     *
     * @param model the model of the document to save
     * @return {@code true} if the document was saved, {@code false} otherwise
     */
    private boolean saveAs(SingleDocumentModel model) {
        int chosenOption = fileChooser.showSaveDialog(JNotepadPP.this);

        if (chosenOption != JFileChooser.APPROVE_OPTION)
            return false;

        Path chosenPath = fileChooser.getSelectedFile().toPath();

        if (Files.exists(chosenPath)) {
            int response = localizedConfirmDialog(JOptionPane.WARNING_MESSAGE, "file.saveAs.confirm",
                    chosenPath.getFileName().toString());

            if (response != 0)
                return false;
        }

        return trySave(model, chosenPath);
    }

    /**
     * Tries to save a document to disk, and in case of failure shows a message dialog.
     * <p>
     * The document is saved using
     * {@link hr.fer.oprpp1.hw08.jnotepadpp.documentModel.MultipleDocumentModel#saveDocument(SingleDocumentModel, Path)}.
     *
     * @param model   the model of the document to save
     * @param newPath the document's new path, or {@code null}
     * @return {@code true} if the document was saved, {@code false} otherwise
     * @throws NullPointerException if {@code model} is {@code null}
     */
    private boolean trySave(SingleDocumentModel model, Path newPath) {
        try {
            documents.saveDocument(model, newPath);
        } catch (UncheckedIOException e) {
            localizedMessageDialog(JOptionPane.ERROR_MESSAGE, "file.save.error.io");
            return false;
        } catch (ConcurrentModificationException e) {
            localizedMessageDialog(JOptionPane.ERROR_MESSAGE, "file.save.error.isOpen");
            return false;
        }

        return true;
    }

    /**
     * Tries to close the currently open document.
     * <p>
     * If the document has unsaved changes, the user is prompted to save or discard them.
     *
     * @return {@code true} if the document was closed, {@code false} otherwise
     * (i.e. if it had unsaved changes, but the user cancelled the confirmation dialog).
     */
    private boolean tryClose() {
        SingleDocumentModel model = documents.getCurrentDocument();

        if (!model.isModified()) {
            documents.closeDocument(model);
            return true;
        }

        String fileName = model.getFilePath() != null ?
                model.getFilePath().getFileName().toString() : localizationProvider.getString("document.unnamed");

        int response = localizedConfirmDialog(JOptionPane.WARNING_MESSAGE, "file.close.confirm", fileName);

        switch (response) {
            case 0:
                boolean result = model.getFilePath() != null ? trySave(model, null) : saveAs(model);
                if (!result)
                    return false;
                // Fallthrough
            case 1:
                documents.closeDocument(model);
                return true;

            default:
                return false;
        }
    }

    /**
     * Tries to close all open documents and subsequently close the window.
     * <p>
     * Documents are closed using {@link #tryClose()} and if closing one fails, exiting is aborted.
     */
    private void tryExit() {
        while (documents.getNumberOfDocuments() > 0)
            if (!tryClose())
                return;

        dispose();
    }

    /**
     * Updates the window title with the path of the currently open document.
     */
    private void updateTitle() {
        if (documents.getNumberOfDocuments() == 0) {
            setTitle(localizationProvider.getString("app.name"));
            return;
        }

        SingleDocumentModel model = documents.getCurrentDocument();
        String tabTooltip = model.getFilePath() != null ?
                model.getFilePath().toAbsolutePath().toString() :
                localizationProvider.getString("document.unnamed");

        setTitle(tabTooltip + " - " + localizationProvider.getString("app.name"));
    }

    /**
     * Shows a localized message dialog.
     * <p>
     * The dialog message is created using {@link String#format(String, Object...)}.
     * The localized string corresponding to {@code messageKey} is used as the format string.
     * The {@code formatArgs} array is used for the format argument without any modification.
     *
     * @param messageType designates what kind of message this is
     * @param messageKey  the translation key used to get the format string for the message
     * @param formatArgs  the format arguments for the message
     * @see JOptionPane#showMessageDialog(Component, Object, String, int)
     */
    private void localizedMessageDialog(int messageType, String messageKey, Object... formatArgs) {
        String[] options = new String[]{
                localizationProvider.getString("dialog.ok"),
        };

        JOptionPane.showOptionDialog(JNotepadPP.this,
                String.format(localizationProvider.getString(messageKey), formatArgs),
                localizationProvider.getString("app.name"),
                JOptionPane.DEFAULT_OPTION, messageType, null, options, options[0]);
    }

    /**
     * Shows a localized confirmation dialog with the options <i>yes</i>, <i>no</i> and <i>cancel</i>.
     * <p>
     * The dialog message is created using {@link String#format(String, Object...)}.
     * The localized string corresponding to {@code messageKey} is used as the format string.
     * The {@code formatArgs} array is used for the format argument without any modification.
     *
     * @param messageType designates what kind of message this is
     * @param messageKey  the translation key used to get the format string for the message
     * @param formatArgs  the format arguments for the message
     * @return {@code 0} if <i>yes</i> was chosen, {@code 1} if <i>no</i> was chosen, {@code 2} if <i>cancel</i> was
     * chosen, or {@link JOptionPane#CLOSED_OPTION} if the dialog was closed without choosing an option.
     * @see JOptionPane#showConfirmDialog(Component, Object, String, int, int)
     */
    private int localizedConfirmDialog(int messageType, String messageKey, Object... formatArgs) {
        String[] options = new String[]{
                localizationProvider.getString("dialog.yes"),
                localizationProvider.getString("dialog.no"),
                localizationProvider.getString("dialog.cancel"),
        };

        return JOptionPane.showOptionDialog(JNotepadPP.this,
                String.format(localizationProvider.getString(messageKey), formatArgs),
                localizationProvider.getString("app.name"),
                JOptionPane.DEFAULT_OPTION, messageType, null, options, options[2]);
    }

    /**
     * Creates a new JNotepad++ window.
     *
     * @param args unused
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LocalizationProvider.getInstance().setLanguage(DEFAULT_LANGUAGE);
            new JNotepadPP().setVisible(true);
        });
    }
}