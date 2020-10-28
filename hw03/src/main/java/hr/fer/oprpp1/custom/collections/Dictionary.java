package hr.fer.oprpp1.custom.collections;

import java.util.Objects;

/**
 * An object that maps keys to values. A map cannot contain duplicate keys; each key can map to at most one value.
 * <p>
 * {@code null} is allowed as a value, but not as a key.
 * <p>
 * Keys are compared using {@link Object#equals(Object)}.
 *
 * @param <K> the type of the keys
 * @param <V> the type of the values
 * @author Borna Cafuk
 */
public class Dictionary<K, V> {
    /**
     * The internal list of collections.
     */
    private ArrayIndexedCollection<Entry<K, V>> entries = new ArrayIndexedCollection<>();

    /**
     * Checks whether there currently are any entries in the dictionary.
     *
     * @return {@code true} if the dictionary contains at least one key-value pair, {@code false} otherwise
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Gets the number of entries currently stored in the dictionary.
     *
     * @return the number of key-value contained in the dictionary
     */
    public int size() {
        return entries.size();
    }

    /**
     * Removes all key-value pairs from the dictionary.
     */
    public void clear() {
        entries.clear();
    }

    /**
     * Assigns a value to a given key. If the key already exists in the map, its value will be overwritten.
     *
     * @param key   the key whose value to assign
     * @param value the new value for the key
     * @return the value which was assigned to the key previously if the key already exists, {@code null} otherwise
     * @throws NullPointerException if {@code key} is {@code null}
     */
    public V put(K key, V value) {
        Objects.requireNonNull(key, "The key must not be null.");
        Entry<K, V> entry = getEntry(key);

        if (entry == null) {
            entries.add(new Entry<>(key, value));
            return null;
        }

        V oldValue = entry.value;
        entry.value = value;
        return oldValue;
    }

    /**
     * Retrieves the value associated with a key.
     *
     * @param key the key whose value to look up
     * @return the value assigned to the given key, or {@code null} if the key does not exist in the dictionary
     */
    public V get(Object key) {
        Entry<K, V> entry = getEntry(key);

        if (entry == null)
            return null;

        return entry.value;
    }

    /**
     * Removes a key-value pair from the dictionary.
     *
     * @param key the key to remove from the dictionary
     * @return the value which was associated with the given key,
     *         or {@code null} if the key did not exist in the dictionary
     */
    public V remove(Object key) {
        Entry<K, V> entry = getEntry(key);

        if (entry == null)
            return null;

        entries.remove(entry);
        return entry.value;
    }

    /**
     * Searches the dictionary for the entry with the given key.
     *
     * @param key the key to search for
     * @return the entry with the given key if it exists in the dictionary, {@code null} otherwise
     */
    private Entry<K, V> getEntry(Object key) {
        ElementsGetter<Entry<K, V>> getter = entries.createElementsGetter();

        while (getter.hasNextElement()) {
            Entry<K, V> entry = getter.getNextElement();
            if (entry.key.equals(key))
                return entry;
        }

        return null;
    }

    /**
     * A key-value pair.
     *
     * @param <K> the type of the key
     * @param <V> the type of the value
     */
    private static class Entry<K, V> {
        /**
         * The key.
         */
        public K key;
        /**
         * The value.
         */
        public V value;

        /**
         * Constructs a new entry with the given key and value.
         *
         * @param key   the key
         * @param value the value
         */
        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        /**
         * Compares two entries with respect to their keys. Keys are compared using {@link Object#equals(Object)}.
         *
         * @param o the object to which to compare the entry
         * @return {@code true} if {@code o} is an {@link Entry} and its key is equal to this entry's key,
         *         {@code false} otherwise
         */
        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;

            Entry<?, ?> entry = (Entry<?, ?>) o;
            return key.equals(entry.key);
        }
    }
}
