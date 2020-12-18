package hr.fer.zemris.java.gui.prim;

import javax.swing.*;
import java.awt.*;
import java.io.Serial;

/**
 * Demonstrates a custom list model containing prime numbers (and the non-prime 1).
 *
 * @author Borna Cafuk
 */
public class PrimDemo extends JFrame {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new window demonstrating the custom list model.
     */
    public PrimDemo() {
        setSize(300, 200);
        setTitle("PrimDemo");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        initGUI();
    }

    /**
     * Initializes the window's GUI with two lists and a button which adds the next prime number.
     */
    private void initGUI() {
        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());

        PrimListModel model = new PrimListModel();

        JList<Integer> list1 = new JList<>(model);
        JList<Integer> list2 = new JList<>(model);

        JPanel central = new JPanel(new GridLayout(1, 0));
        central.add(new JScrollPane(list1));
        central.add(new JScrollPane(list2));
        cp.add(central, BorderLayout.CENTER);

        JButton nextButton = new JButton("Sljedeći");
        nextButton.addActionListener(e -> model.next());
        cp.add(nextButton, BorderLayout.PAGE_END);
    }

    /**
     * Creates a new {@code PrimDemo} window and displays it.
     *
     * @param args unused
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new PrimDemo();
            frame.setVisible(true);
        });
    }
}
