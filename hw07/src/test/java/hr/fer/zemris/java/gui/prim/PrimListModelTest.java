package hr.fer.zemris.java.gui.prim;

import org.junit.jupiter.api.Test;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class PrimListModelTest {
    @Test
    public void testConstructor() {
        PrimListModel model = new PrimListModel();

        assertEquals(1, model.getSize());
        assertEquals(1, model.getElementAt(0));
    }

    @Test
    public void testNext() {
        int[] expectedContents = new int[]{1, 2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67,
                71, 73, 79, 83, 89, 97, 101, 103, 107, 109, 113, 127, 131, 137, 139, 149, 151, 157, 163, 167, 173, 179,
                181, 191, 193, 197, 199, 211, 223, 227, 229, 233, 239, 241, 251, 257, 263, 269, 271};

        PrimListModel model = new PrimListModel();
        for (int i = 0; i < expectedContents.length - 1; i++)
            model.next();

        assertEquals(expectedContents.length, model.getSize());
        for (int i = 0; i < expectedContents.length; i++) {
            assertEquals(expectedContents[i], model.getElementAt(i));
        }
    }

    @Test
    void testDataListener() {
        final int ADDITION_COUNT = 100;

        PrimListModel model = new PrimListModel();

        TestListener listener = new TestListener();
        model.addListDataListener(listener);

        for (int i = 0; i < ADDITION_COUNT; i++)
            model.next();
        assertEquals(ADDITION_COUNT + 1, listener.expectedIndex);
    }

    @Test
    void testRemoveDataListener() {
        final int LIMITED_1_ADDITIONS = 100;
        final int LIMITED_2_ADDITIONS = 200;
        final int LIMITED_3_ADDITIONS = 300;

        PrimListModel model = new PrimListModel();

        FailListener fl1 = new FailListener();
        model.addListDataListener(fl1);

        LimitedListener ll1 = new LimitedListener(LIMITED_1_ADDITIONS);
        model.addListDataListener(ll1);

        LimitedListener ll2 = new LimitedListener(LIMITED_2_ADDITIONS);
        model.addListDataListener(ll2);

        FailListener fl2 = new FailListener();
        model.addListDataListener(fl2);

        LimitedListener ll3 = new LimitedListener(LIMITED_3_ADDITIONS);
        model.addListDataListener(ll3);

        FailListener fl3 = new FailListener();
        model.addListDataListener(fl3);

        int i = 0;

        model.removeListDataListener(fl1);
        model.removeListDataListener(fl2);
        model.removeListDataListener(fl3);

        while (i < LIMITED_1_ADDITIONS) {
            model.next();
            i++;
        }

        ll1.assertDone();
        model.removeListDataListener(ll1);

        while (i < LIMITED_2_ADDITIONS) {
            model.next();
            i++;
        }

        ll2.assertDone();
        model.removeListDataListener(ll2);

        while (i < LIMITED_3_ADDITIONS) {
            model.next();
            i++;
        }

        ll3.assertDone();
        model.removeListDataListener(ll3);
    }

    private static class TestListener implements ListDataListener {
        private int expectedIndex = 1;

        @Override
        public void intervalAdded(ListDataEvent e) {
            assertEquals(expectedIndex, e.getIndex0());
            assertEquals(expectedIndex, e.getIndex1());

            expectedIndex++;
        }

        @Override
        public void intervalRemoved(ListDataEvent e) {
            fail();
        }

        @Override
        public void contentsChanged(ListDataEvent e) {
            fail();
        }
    }

    private static class FailListener implements ListDataListener {
        @Override
        public void intervalAdded(ListDataEvent e) {
            fail();
        }

        @Override
        public void intervalRemoved(ListDataEvent e) {
            fail();
        }

        @Override
        public void contentsChanged(ListDataEvent e) {
            fail();
        }
    }

    private static class LimitedListener implements ListDataListener {
        private int remainingAdditions;

        public LimitedListener(int remainingAdditions) {
            this.remainingAdditions = remainingAdditions;
        }

        public void assertDone() {
            assertEquals(0, remainingAdditions);
        }

        @Override
        public void intervalAdded(ListDataEvent e) {
            if (remainingAdditions > 0)
                remainingAdditions--;
            else
                fail();
        }

        @Override
        public void intervalRemoved(ListDataEvent e) {
            fail();
        }

        @Override
        public void contentsChanged(ListDataEvent e) {
            fail();
        }
    }
}
