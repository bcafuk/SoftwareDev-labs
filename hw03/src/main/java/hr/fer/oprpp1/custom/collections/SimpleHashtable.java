package hr.fer.oprpp1.custom.collections;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
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
public class SimpleHashtable<K, V> implements Iterable<SimpleHashtable.TableEntry<K, V>> {
    /**
     * The default initial number of buckets if not specified otherwise.
     */
    private static final int DEFAULT_CAPACITY = 16;
    /**
     * The maximum ratio of entries to buckets.
     * Must be greater than 0.
     */
    private static final double LOAD_FACTOR = 0.75;
    /**
     * The factor by which the size of {@link #table} is increased in {@link #resize()}.
     * Must be a power of two.
     */
    private static final int GROWTH_FACTOR = 2;

    /**
     * An array of buckets holding the data.
     * Each slot in the array is either the head of the linked list of entries in that bucket,
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
     * <p>
     * If adding a new element would exceed the load factor of {@value #LOAD_FACTOR},
     * a resize to {@value #GROWTH_FACTOR} times the current number of buckets is triggered.
     *
     * @param key   the key whose value to assign
     * @param value the new value for the key
     * @return the value which was assigned to the key previously if the key already exists, {@code null} otherwise
     * @throws NullPointerException if {@code key} is {@code null}
     */
    public V put(K key, V value) {
        if ((size + 1.0d) / table.length > LOAD_FACTOR)
            resize();

        Objects.requireNonNull(key, "The key must not be null.");

        int bucketIndex = getBucket(key);
        TableEntry<K, V> entry = table[bucketIndex];

        if (entry == null) {
            // The bucket is empty
            table[bucketIndex] = new TableEntry<>(key, value);
            size++;
            return null;
        }

        TableEntry<K, V> prevEntry = null;

        while (entry != null) {
            if (key.equals(entry.key)) {
                // The key exists
                V oldValue = entry.value;
                entry.value = value;
                return oldValue;
            }
            prevEntry = entry;
            entry = entry.next;
        }

        // The bucket isn't empty, but the key doesn't exist
        prevEntry.next = new TableEntry<>(key, value);
        size++;
        return null;
    }

    /**
     * Retrieves the value associated with a key.
     *
     * @param key the key whose value to look up
     * @return the value assigned to the given key, or {@code null} if the key does not exist in the hashtable
     */
    public V get(Object key) {
        TableEntry<K, V> entry = findByKey(key);

        if (entry == null)
            return null;

        return entry.value;
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
        return findByKey(key) != null;
    }

    /**
     * Checks if the hashtable contains an entry with the given value.
     *
     * @param value the key to look up
     * @return {@code true} if the hashtable contains the given key, {@code false} otherwise
     */
    public boolean containsValue(Object value) {
        for (TableEntry<K, V> kvTableEntry : this)
            if (Objects.equals(value, kvTableEntry.value))
                return true;

        return false;
    }

    /**
     * Removes the entry with the given key from the hashtable.
     *
     * @param key the key of the entry to remove
     * @return the value which was assigned to the key
     */
    public V remove(Object key) {
        if (key == null)
            return null;

        int bucketIndex = getBucket(key);

        if (table[bucketIndex] == null)
            // The bucket is empty
            return null;

        if (key.equals(table[bucketIndex].key)) {
            // The first entry in the bucket has the key
            V oldValue = table[bucketIndex].value;
            table[bucketIndex] = table[bucketIndex].next;
            size--;
            return oldValue;
        }

        TableEntry<K, V> prevEntry = table[bucketIndex];

        while (prevEntry.next != null) {
            if (key.equals(prevEntry.next.key)) {
                // An entry other than the first one has the key
                V oldValue = prevEntry.next.value;
                prevEntry.next = prevEntry.next.next;
                size--;
                return oldValue;
            }
            prevEntry = prevEntry.next;
        }

        // The bucket isn't empty, but the key doesn't exist
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
        StringBuilder sb = new StringBuilder("[");

        Iterator<TableEntry<K, V>> iterator = iterator();

        while (iterator.hasNext()) {
            sb.append(iterator.next());

            if (iterator.hasNext())
                sb.append(", ");
        }

        sb.append(']');

        return sb.toString();
    }

    /**
     * Dumps all key-value pairs currently stored in the hashtable into an array.
     * <p>
     * No guarantee is given about the order of the elements, except that two calls to the function
     * will yield the same order if the hashtable is not modified between them.
     *
     * @return an array of the entries stored in the hashtable.
     */
    @SuppressWarnings("unchecked")
    public TableEntry<K, V>[] toArray() {
        TableEntry<K, V>[] array = (TableEntry<K, V>[]) new TableEntry[size];

        Iterator<TableEntry<K, V>> iterator = iterator();
        for (int i = 0; i < array.length; i++) {
            array[i] = iterator.next();
        }

        return array;
    }

    /**
     * Removes all entries from the hashtable.
     */
    public void clear() {
        Arrays.fill(table, null);
        size = 0;
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
     * @throws NullPointerException if {@code key} is {@code null}
     */
    private int getBucket(Object key) {
        Objects.requireNonNull(key, "The key must not be null");
        return Math.abs(key.hashCode()) % table.length;
    }

    /**
     * Finds the entry with the given key.
     *
     * @param key the key look up
     * @return the entry with the given key if it exists in the hashmap, {@code null} otherwise
     */
    private TableEntry<K, V> findByKey(Object key) {
        if (key == null)
            return null;

        int bucketIndex = getBucket(key);
        TableEntry<K, V> entry = table[bucketIndex];

        if (entry == null)
            // The bucket is empty
            return null;

        while (entry != null) {
            if (key.equals(entry.key))
                // The key exists
                return entry;

            entry = entry.next;
        }

        // The bucket isn't empty, but the key doesn't exist
        return null;
    }

    /**
     * Returns an iterator over the entries of the hashtable.
     *
     * @return a new iterator
     */
    @Override
    public Iterator<TableEntry<K, V>> iterator() {
        return new IteratorImpl();
    }

    /**
     * Increases the number of buckets {@value GROWTH_FACTOR} times and reinserts all existing entries.
     */
    @SuppressWarnings("unchecked")
    private void resize() {
        TableEntry<K, V>[] entries = toArray();

        table = (TableEntry<K, V>[]) new TableEntry[table.length * GROWTH_FACTOR];
        size = 0;

        for (TableEntry<K, V> entry : entries)
            put(entry.key, entry.value);
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
    public class IteratorImpl implements Iterator<TableEntry<K, V>> {
        /**
         * The index of the bucket to which {@link #nextEntry} belongs.
         * If there are no more elements, it will be equal to the number of buckets.
         */
        private int bucketIndex;
        /**
         * The next entry to be returned by {@link #next()}.
         * If there are no more elements, it will be {@code null}.
         */
        private TableEntry<K, V> nextEntry;

        private IteratorImpl() {
            bucketIndex = -1;
            nextEntry = null;
            advance();
        }

        @Override
        public boolean hasNext() {
            return nextEntry != null;
        }

        @Override
        public TableEntry<K, V> next() {
            TableEntry<K, V> entry = nextEntry;
            advance();
            return entry;
        }

        @Override
        public void remove() {
            // TODO: Implement method
            throw new UnsupportedOperationException("remove");
        }

        /**
         * Advances the internal state ({@link #bucketIndex} and {@link #nextEntry}) to the next entry in the hashtable.
         * <p>
         * If called when {@link #nextEntry} is the last entry,
         * {@link #bucketIndex} will be set to the number of buckets in the hashmap,
         * and {@link #nextEntry} will be set to {@code null}.
         * <p>
         * If called with {@link #bucketIndex} set to -1 and {@link #nextEntry} set to {@code null},
         * it will set {@link #nextEntry} to the first entry in the hashtable.
         *
         * @throws NoSuchElementException if called when there are no more entries in the hashtable
         */
        private void advance() {
            if (bucketIndex >= table.length)
                throw new NoSuchElementException("All entries have been iterated over.");

            if (nextEntry != null)
                nextEntry = nextEntry.next;

            while (nextEntry == null) {
                bucketIndex++;
                if (bucketIndex >= table.length)
                    break;

                nextEntry = table[bucketIndex];
            }
        }
    }
}
