package hr.fer.oprpp1.custom.collections;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class SimpleHashtableTest {
    @Test
    public void testDefaultConstructor() {
        SimpleHashtable<String, Integer> hashtable = new SimpleHashtable<>();
        assertTrue(hashtable.isEmpty());
    }

    @Test
    public void testInitialCapacityConstructor() {
        SimpleHashtable<String, Integer> hashtable = new SimpleHashtable<>(17);
        assertTrue(hashtable.isEmpty());
    }

    @Test
    public void testInitialCapacityConstructorWithIllegalCapacity() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new SimpleHashtable<String, Integer>(-1)
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new SimpleHashtable<String, Integer>(0)
        );
    }

    @Test
    public void testPut() {
        SimpleHashtable<String, Integer> hashtable = new SimpleHashtable<>();
        HashMap<String, Integer> expected = new HashMap<>();

        for (int i = 0; i < 100; i++) {
            assertFalse(hashtable.containsKey("Key" + i));
            assertNull(hashtable.put("Key" + i, i));
            assertEquals(i + 1, hashtable.size());
            assertTrue(hashtable.containsKey("Key" + i));

            expected.put("Key" + i, i);
        }

        assertHashtableEquals(expected, hashtable);
    }

    @Test
    public void testPutNullKey() {
        SimpleHashtable<String, Integer> hashtable = new SimpleHashtable<>();

        assertThrows(NullPointerException.class, () -> hashtable.put(null, 0));
    }

    @Test
    public void testPutNullValue() {
        SimpleHashtable<String, Integer> hashtable = new SimpleHashtable<>();

        assertFalse(hashtable.containsValue(null));
        hashtable.put("Key", null);
        assertTrue(hashtable.containsValue(null));
    }

    @Test
    public void testPutWithExistingKey() {
        SimpleHashtable<String, Integer> hashtable = new SimpleHashtable<>();

        for (int i = 0; i < 100; i++)
            hashtable.put("Key" + i, i);

        assertEquals(100, hashtable.size());
        HashMap<String, Integer> expected = new HashMap<>();

        for (int i = 0; i < 100; i++) {
            assertEquals(i, hashtable.put("Key" + i, -i));
            assertEquals(100, hashtable.size());

            expected.put("Key" + i, -i);
        }

        assertHashtableEquals(expected, hashtable);
    }

    @Test
    public void testGet() {
        SimpleHashtable<String, Integer> hashtable = new SimpleHashtable<>();

        for (int i = 0; i < 100; i++)
            hashtable.put("Key" + i, i);

        for (int i = 0; i < 100; i++)
            assertEquals(i, hashtable.get("Key" + i));
    }

    @Test
    public void testGetWithNonexistentKey() {
        SimpleHashtable<String, Integer> hashtable = new SimpleHashtable<>();

        assertNull(hashtable.get("Key"));
    }

    @Test
    public void testContainsKey() {
        SimpleHashtable<String, Integer> hashtable = new SimpleHashtable<>();

        for (int i = 0; i < 200; i++)
            assertFalse(hashtable.containsKey("Key" + i));

        for (int i = 0; i < 100; i++)
            hashtable.put("Key" + i, i);

        for (int i = 0; i < 100; i++)
            assertTrue(hashtable.containsKey("Key" + i));

        for (int i = 100; i < 200; i++)
            assertFalse(hashtable.containsKey("Key" + i));
    }

    @Test
    public void testContainsValue() {
        SimpleHashtable<String, Integer> hashtable = new SimpleHashtable<>();

        for (int i = 0; i < 200; i++)
            assertFalse(hashtable.containsValue(i));

        for (int i = 0; i < 100; i++)
            hashtable.put("Key" + i, i);

        for (int i = 0; i < 100; i++)
            assertTrue(hashtable.containsValue(i));

        for (int i = 100; i < 200; i++)
            assertFalse(hashtable.containsValue(i));
    }

    @Test
    public void testRemove() {
        SimpleHashtable<String, Integer> hashtable = new SimpleHashtable<>();

        for (int i = 0; i < 100; i++)
            hashtable.put("Key" + i, i);

        for (int i = 0; i < 100; i++) {
            assertTrue(hashtable.containsKey("Key" + i));
            assertEquals(i, hashtable.remove("Key" + i));
            assertEquals(99 - i, hashtable.size());
            assertFalse(hashtable.containsKey("Key" + i));
        }

        assertTrue(hashtable.isEmpty());
    }

    @Test
    public void testRemoveWithNonexistentKey() {
        SimpleHashtable<String, Integer> hashtable = new SimpleHashtable<>();

        assertNull(hashtable.remove("Key"));
    }

    @Test
    public void testToArray() {
        SimpleHashtable<String, Integer> hashtable = new SimpleHashtable<>();
        HashMap<String, Integer> expected = new HashMap<>();

        for (int i = 0; i < 100; i++) {
            hashtable.put("Key" + i, i);
            expected.put("Key" + i, i);
        }

        Map<String, Integer> actualMap = new HashMap<>(hashtable.size());
        for (SimpleHashtable.TableEntry<String, Integer> entry : hashtable.toArray())
            actualMap.put(entry.getKey(), entry.getValue());

        assertEquals(expected, actualMap);
    }

    @Test
    public void testClear() {
        SimpleHashtable<String, Integer> hashtable = new SimpleHashtable<>();

        for (int i = 0; i < 100; i++)
            hashtable.put("Key" + i, i);

        assertFalse(hashtable.isEmpty());
        assertEquals(100, hashtable.size());

        hashtable.clear();

        assertTrue(hashtable.isEmpty());
        assertEquals(0, hashtable.size());
    }

    @Test
    public void testIterator() {
        SimpleHashtable<String, Integer> hashtable = new SimpleHashtable<>();

        for (int i = 0; i < 100; i++)
            hashtable.put("Key" + i, i);

        Iterator<SimpleHashtable.TableEntry<String, Integer>> iterator = hashtable.iterator();

        Set<String> iteratedKeys = new HashSet<>();
        for (int i = 0; i < 100; i++) {
            assertTrue(iterator.hasNext());
            SimpleHashtable.TableEntry<String, Integer> entry = iterator.next();

            assertEquals("Key" + entry.getValue(), entry.getKey());
            assertFalse(iteratedKeys.contains(entry.getKey()));

            iteratedKeys.add(entry.getKey());
        }

        assertEquals(100, iteratedKeys.size());

        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    public void testIteratorsWithConcurrentModification() {
        SimpleHashtable<String, Integer> hashtable = new SimpleHashtable<>();

        for (int i = 0; i < 100; i++)
            hashtable.put("Key" + i, i);

        Iterator<SimpleHashtable.TableEntry<String, Integer>> iteratorPut = hashtable.iterator();
        hashtable.put("Key100", 100);
        // Put has added a new entry
        assertThrows(ConcurrentModificationException.class, iteratorPut::hasNext);
        assertThrows(ConcurrentModificationException.class, iteratorPut::next);
        assertThrows(ConcurrentModificationException.class, iteratorPut::remove);

        Iterator<SimpleHashtable.TableEntry<String, Integer>> iteratorPutExisting = hashtable.iterator();
        hashtable.put("Key100", 101);
        // Put has changed an existing entry's value
        // This may throw if a resize occurred! Please check the load factor
        assertDoesNotThrow(iteratorPutExisting::hasNext);
        assertDoesNotThrow(iteratorPutExisting::next);
        assertDoesNotThrow(iteratorPutExisting::remove);

        Iterator<SimpleHashtable.TableEntry<String, Integer>> iteratorRemove = hashtable.iterator();
        hashtable.remove("Key100");
        // Remove has removed an existing entry
        assertThrows(ConcurrentModificationException.class, iteratorRemove::hasNext);
        assertThrows(ConcurrentModificationException.class, iteratorRemove::next);
        assertThrows(ConcurrentModificationException.class, iteratorRemove::remove);

        Iterator<SimpleHashtable.TableEntry<String, Integer>> iteratorRemoveNonexitent = hashtable.iterator();
        hashtable.remove("Key100");
        // Remove has not removed an entry because it doesn't exist
        assertDoesNotThrow(iteratorRemoveNonexitent::hasNext);
        assertDoesNotThrow(iteratorRemoveNonexitent::next);
        assertDoesNotThrow(iteratorRemoveNonexitent::remove);

        Iterator<SimpleHashtable.TableEntry<String, Integer>> iteratorClear = hashtable.iterator();
        hashtable.clear();
        // Clear has removed all elements
        assertThrows(ConcurrentModificationException.class, iteratorClear::hasNext);
        assertThrows(ConcurrentModificationException.class, iteratorClear::next);
        assertThrows(ConcurrentModificationException.class, iteratorClear::remove);
    }

    @Test
    public void testIteratorRemove() {
        SimpleHashtable<String, Integer> hashtable = new SimpleHashtable<>();

        for (int i = 0; i < 100; i++)
            hashtable.put("Key" + i, i);

        assertTrue(hashtable.containsKey("Key17"));
        assertEquals(100, hashtable.size());

        Iterator<SimpleHashtable.TableEntry<String, Integer>> iterator1 = hashtable.iterator();
        while (iterator1.hasNext())
            if (iterator1.next().getKey().equals("Key17"))
                iterator1.remove();

        assertFalse(hashtable.containsKey("Key17"));
        assertEquals(99, hashtable.size());

        Iterator<SimpleHashtable.TableEntry<String, Integer>> iterator2 = hashtable.iterator();
        while (iterator2.hasNext()) {
            iterator2.next();
            iterator2.remove();
        }

        assertTrue(hashtable.isEmpty());
        assertEquals(0, hashtable.size());
    }

    @Test
    public void testIteratorRemoveWhenIllegal() {
        SimpleHashtable<String, Integer> hashtable = new SimpleHashtable<>();

        for (int i = 0; i < 100; i++)
            hashtable.put("Key" + i, i);

        Iterator<SimpleHashtable.TableEntry<String, Integer>> iterator = hashtable.iterator();

        // Calling remove() before next()
        assertThrows(IllegalStateException.class, iterator::remove);

        while (iterator.hasNext()) {
            if (iterator.next().getKey().equals("Key17")) {
                assertDoesNotThrow(iterator::remove);
                // Calling remove() twice for the same call of next()
                assertThrows(IllegalStateException.class, iterator::remove);
            }
        }
    }

    private static <K, V> void assertHashtableEquals(Map<K, V> expected, SimpleHashtable<K, V> actual) {
        Map<K, V> actualMap = new HashMap<>(actual.size());
        for (SimpleHashtable.TableEntry<K, V> entry : actual)
            actualMap.put(entry.getKey(), entry.getValue());

        assertEquals(expected, actualMap);
    }
}
