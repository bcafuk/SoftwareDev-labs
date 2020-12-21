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
        int p = numbers.get(numbers.size() - 1) + 1;
        while (!isPrime(p))
            p++;

        numbers.add(p);
        notifyAboutAddition(numbers.size() - 1);
    }

    /**
     * Checks if a number is prime.
     *
     * @param n the number to check
     * @return {@code true} if {@code n} is prime, {@code false} otherwise
     */
    private static boolean isPrime(int n) {
        if (n < 2)
            return false;

        if (n % 2 == 0)
            return n == 2;

        int rt_n = (int) Math.sqrt(n);
        for (int i = 3; i <= rt_n; i += 2)
            if (n % i == 0)
                return false;

        return true;
    }

    /**
     * Notifies all subscribed listeners that a new number has been added to the list at a given index.
     *
     * @param index the index of the added number
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
