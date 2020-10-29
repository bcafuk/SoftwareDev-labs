package hr.fer.oprpp1.custom.collections;

import java.util.Iterator;
import java.util.Objects;

/**
 * A table that maps keys to values. A map cannot contain duplicate keys; each key can map to at most one value.
 * <p>
 * {@code null} is allowed as a value, but not as a key.
 * <p>
 * Keys are compared using {@link Object#equals(Object)}.
 * Values are stored in buckets according to the hash of their key, which is determined using {@link Object#hashCode()}.
 *
 * @param <K> the type of the keys
 * @param <V> the type of the values
 * @author Borna Cafuk
 */
public class SimpleHashtable<K, V> {
    /**
     * The default initial number of buckets if not specified otherwise.
     */
    private static final int DEFAULT_CAPACITY = 16;

    /**
     * An array of buckets holding the data.
     * Each slot in the array is either the head of the linked list of entries in that bucker,
     * or {@code null} if the bucket is empty.
     */
    private TableEntry<K, V>[] table;
    /**
     * The number of entries currently stored in the hash table.
     */
    private int size = 0;

    /**
     * Constructs a new hashtable with the default of {@value DEFAULT_CAPACITY} buckets.
     */
    public SimpleHashtable() {
        this(DEFAULT_CAPACITY);
    }

    /**
     * Constructs a new hashtable with at least the specified number of buckets.
     * <p>
     * The actual number of buckets has to be a power of two, so if {@code capacity} is not a power of two,
     * the smallest power of two larger than {@code capacity} will be used.
     *
     * @param capacity the desired number of buckets
     * @throws IllegalArgumentException if {@code capacity} is less than 1
     * @throws IllegalArgumentException if {@code capacity} is larger than the largest power of two
     *                                  representable by an {@link Integer}
     */
    @SuppressWarnings("unchecked")
    public SimpleHashtable(int capacity) {
        if (capacity < 1)
            throw new IllegalArgumentException("The initial number of buckets has to be at least one, but " + capacity + " was given.");

        table = (TableEntry<K, V>[]) new TableEntry[findFirstPowerOfTwo(capacity)];
    }

    /**
     * Assigns a value to a given key.
     * If the key already exists in the hashtable, its value will be overwritten.
     * Otherwise, a new entry is added to the hashtable.
     *
     * @param key   the key whose value to assign
     * @param value the new value for the key
     * @return the value which was assigned to the key previously if the key already exists, {@code null} otherwise
     * @throws NullPointerException if {@code key} is {@code null}
     */
    public V put(K key, V value) {
        Objects.requireNonNull(key, "The key must not be null.");

        // TODO: Implement method
        return null;
    }

    /**
     * Retrieves the value associated with a key.
     *
     * @param key the key whose value to look up
     * @return the value assigned to the given key, or {@code null} if the key does not exist in the hashtable
     */
    public V get(Object key) {
        // TODO: Implement method
        return null;
    }

    /**
     * Gets the number of entries stored in the hashtable.
     *
     * @return the number of key-value pairs currently stored in the hashtable
     */
    public int size() {
        return size;
    }

    /**
     * Checks if the hashtable contains an entry with the given key.
     *
     * @param key the key to look up
     * @return {@code true} if the hashtable contains the given key, {@code false} otherwise
     */
    public boolean containsKey(Object key) {
        // TODO: Implement method
        return false;
    }

    /**
     * Checks if the hashtable contains an entry with the given value.
     *
     * @param value the key to look up
     * @return {@code true} if the hashtable contains the given key, {@code false} otherwise
     */
    public boolean containsValue(Object value) {
        // TODO: Implement method
        return false;
    }

    /**
     * Removes the entry with the given key from the hashtable.
     *
     * @param key the key of the entry to remove
     * @return the value which was assigned to the key
     */
    public V remove(Object key) {
        // TODO: Implement method
        return null;
    }

    /**
     * Checks whether there are no entries in the hashtable.
     *
     * @return {@code true} if there are no entries currently stored in the table, {@code false} otherwise
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Converts the entries of this hashmap into a string in the form of a list of ket-value pairs.
     *
     * @return a string representing the entries of this hashmap
     */
    @Override
    public String toString() {
        // TODO: Implement method
        return null;
    }

    /**
     * Dumps all key-value pairs currently stored in the hashtable into an array.
     * <p>
     * No guarantee is given about the order of the elements, except that two calls to the function
     * will yield the same order if the hashtable is not modified between them.
     *
     * @return an array of the entries stored in the hashtable.
     */
    public TableEntry<K, V>[] toArray() {
        // TODO: Implement method
        return null;
    }

    /**
     * Finds the smallest integer which is both a power of two and larger than or equal to a given parameter.
     *
     * @param n the lower bound for the power of two
     * @return the smallest power of two which is larger or equal to {@code n}
     */
    private int findFirstPowerOfTwo(int n) {
        for (int power = 1; power != 0; power *= 2)
            if (power >= n)
                return power;
        throw new IllegalArgumentException("Couldn't find a power of two greater than " + n + " that fits into an integer.");
    }

    /**
     * Calculates the index of the corresponding bucket for a given key.
     *
     * @param key the key for which to find the bucket
     * @return the index of the bucket
     */
    private int getBucket(Object key) {
        return Math.abs(key.hashCode()) % table.length;
    }

    /**
     * Finds the entry with the given key.
     *
     * @param key the key look up
     * @return the entry with the given key if it exists in the hashmap, {@code null} otherwise
     */
    private TableEntry<K, V> findByKey(Object key) {
        // TODO: Implement method
        return null;
    }

    /**
     * Returns an iterator over the entries of the hashtable.
     *
     * @return a new iterator
     */
    private IteratorImpl iterator() {
        // TODO: Implement method
        return null;
    }

    /**
     * A key-value pair stored in the hashtable.
     *
     * @param <K> the type of the key
     * @param <V> the type of the value
     */
    public static class TableEntry<K, V> {
        /**
         * The entry's key.
         */
        private K key;
        /**
         * The entry's value.
         */
        private V value;
        /**
         * The next entry in the same bucket, or {@code null} if this entry is the last in the bucket.
         */
        private TableEntry<K, V> next = null;

        /**
         * Constructs a new entry with the given parameters.
         *
         * @param key   the entry's key
         * @param value the entry's value
         * @throws NullPointerException if {@code key} is {@code null}
         */
        private TableEntry(K key, V value) {
            this.key = Objects.requireNonNull(key, "The key must not be null");
            this.value = value;
        }

        /**
         * Returns the entry's the key.
         *
         * @return the key
         */
        public K getKey() {
            return key;
        }

        /**
         * Returns the entry's the value.
         *
         * @return the value
         */
        public V getValue() {
            return value;
        }

        /**
         * Changes the entry's value.
         *
         * @param value the new value
         */
        public void setValue(V value) {
            this.value = value;
        }

        /**
         * Converts the entry to a string.
         *
         * @return the key and value, joined by an equals sign ({@code =})
         */
        @Override
        public String toString() {
            return key.toString() + '=' + value.toString();
        }
    }

    /**
     * An iterator over the entries of the hashtable.
     */
    private class IteratorImpl implements Iterator<TableEntry<K, V>> {
        @Override
        public boolean hasNext() {
            // TODO: Implement method
            return false;
        }

        @Override
        public TableEntry<K, V> next() {
            // TODO: Implement method
            return null;
        }

        @Override
        public void remove() {
            // TODO: Implement method
            throw new UnsupportedOperationException("remove");
        }
    }
}
