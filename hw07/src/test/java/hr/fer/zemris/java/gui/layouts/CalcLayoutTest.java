package hr.fer.zemris.java.gui.layouts;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CalcLayoutTest {
    @ParameterizedTest
    @CsvSource({"0,0", "0,1", "1,0", "6,7", "5,8", "6,8"})
    public void testOutOfBoundsConstraint(int row, int column) throws InvocationTargetException, InterruptedException {
        SwingUtilities.invokeAndWait(() -> {
            JPanel p = new JPanel(new CalcLayout());
            JLabel l = new JLabel("");
            assertThrows(CalcLayoutException.class, () -> p.add(l, new RCPosition(row, column)));
        });
    }

    @ParameterizedTest
    @CsvSource({"1,2", "1,3", "1,4", "1,5"})
    public void testInvalidConstraint(int row, int column) throws InvocationTargetException, InterruptedException {
        SwingUtilities.invokeAndWait(() -> {
            JPanel p = new JPanel(new CalcLayout());
            JLabel l = new JLabel("");
            assertThrows(CalcLayoutException.class, () -> p.add(l, new RCPosition(row, column)));
        });
    }

    @Test
    public void testMultipleComponentsForSameConstraint() throws InvocationTargetException, InterruptedException {
        SwingUtilities.invokeAndWait(() -> {
            JPanel p = new JPanel(new CalcLayout());

            JLabel l1 = new JLabel("");
            JLabel l2 = new JLabel("");

            p.add(l1, new RCPosition(1, 1));
            assertThrows(CalcLayoutException.class, () -> p.add(l2, new RCPosition(1, 1)));
        });
    }

    @Test
    public void testPreferredSizeForSmallCells() throws InvocationTargetException, InterruptedException {
        SwingUtilities.invokeAndWait(() -> {
            JPanel p = new JPanel(new CalcLayout(2));

            JLabel l1 = new JLabel("");
            l1.setPreferredSize(new Dimension(10, 30));
            JLabel l2 = new JLabel("");
            l2.setPreferredSize(new Dimension(20, 15));

            p.add(l1, new RCPosition(2, 2));
            p.add(l2, new RCPosition(3, 3));

            Dimension dim = p.getPreferredSize();
            assertEquals(152, dim.width);
            assertEquals(158, dim.height);
        });
    }

    @Test
    public void testPreferredSizeForHomeCell() throws InvocationTargetException, InterruptedException {
        SwingUtilities.invokeAndWait(() -> {
            JPanel p = new JPanel(new CalcLayout(2));

            JLabel l1 = new JLabel("");
            l1.setPreferredSize(new Dimension(108, 15));
            JLabel l2 = new JLabel("");
            l2.setPreferredSize(new Dimension(16, 30));

            p.add(l1, new RCPosition(1, 1));
            p.add(l2, new RCPosition(3, 3));

            Dimension dim = p.getPreferredSize();
            assertEquals(152, dim.width);
            assertEquals(158, dim.height);
        });
    }
}
