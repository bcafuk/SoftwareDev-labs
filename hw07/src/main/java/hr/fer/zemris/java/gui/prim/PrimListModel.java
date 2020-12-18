package hr.fer.zemris.java.gui.prim;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Models a list containing prime numbers.
 * <p>
 * The list initially contains the non-prime number 1.
 * Every call to {@link #next()} will add the next prime number to the list.
 */
public class PrimListModel implements ListModel<Integer> {
    /**
     * The list of numbers stored by the model.
     */
    private final List<Integer> numbers = new ArrayList<>(List.of(1));
    /**
     * A list of listeners subscribed to the model.
     */
    private final List<ListDataListener> listeners = new LinkedList<>();

    /**
     * Adds the next prime number to the list.
     */
    public void next() {
        int lastNumber = numbers.get(numbers.size() - 1);
        numbers.add(nextPrimeAfter(lastNumber));

        notifyAboutAddition(numbers.size() - 1);
    }

    /**
     * Finds the smallest prime number greater than {@code n}.
     *
     * @param n the exclusive lower bound for finding the smallest prime
     * @return the smallest prime number larger than {@code n}
     */
    private static int nextPrimeAfter(int n) {
        // TODO: Actually find next prime number
        return 123;
    }

    /**
     * Notifies all subscribed listeners that a new number has been added to the list.
     */
    private void notifyAboutAddition(int index) {
        ListDataEvent e = new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, index, index);
        for (ListDataListener listener : listeners)
            listener.intervalAdded(e);
    }

    @Override
    public int getSize() {
        return numbers.size();
    }

    @Override
    public Integer getElementAt(int index) {
        return numbers.get(index);
    }

    @Override
    public void addListDataListener(ListDataListener l) {
        listeners.add(l);
    }

    @Override
    public void removeListDataListener(ListDataListener l) {
        listeners.remove(l);
    }
}
