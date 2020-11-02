package hr.fer.oprpp1.custom.collections;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DictionaryTest {
    @Test
    public void testIsEmpty() {
        Dictionary<Integer, Integer> dictionary = new Dictionary<>();
        assertTrue(dictionary.isEmpty());
        dictionary.put(0, null);
        assertFalse(dictionary.isEmpty());
    }

    @Test
    public void testSize() {
        Dictionary<Integer, Integer> dictionary = new Dictionary<>();
        assertEquals(0, dictionary.size());

        dictionary.put(0, null);
        assertEquals(1, dictionary.size());

        dictionary.put(1, null);
        assertEquals(2, dictionary.size());

        dictionary.put(2, null);
        assertEquals(3, dictionary.size());

        dictionary.remove(1);
        assertEquals(2, dictionary.size());

        dictionary.remove(0);
        assertEquals(1, dictionary.size());

        dictionary.remove(2);
        assertEquals(0, dictionary.size());
    }

    @Test
    public void testClear() {
        Dictionary<Integer, Integer> dictionary = new Dictionary<>();

        for (int i = 0; i < 10; i++)
            dictionary.put(i, null);

        assertNotEquals(0, dictionary.size());
        dictionary.clear();
        assertEquals(0, dictionary.size());
    }

    @Test
    public void testPutNew() {
        Dictionary<Integer, Integer> dictionary = new Dictionary<>();

        assertEquals(0, dictionary.size());
        for (int i = 0; i < 10; i++)
            assertNull(dictionary.put(i, null));
        assertEquals(10, dictionary.size());
    }

    @Test
    public void testPutNullKey() {
        Dictionary<Integer, Integer> dictionary = new Dictionary<>();

        assertThrows(NullPointerException.class, () -> dictionary.put(null, null));
    }

    @Test
    public void testPutExisting() {
        Dictionary<Integer, Integer> dictionary = new Dictionary<>();
        assertEquals(0, dictionary.size());

        assertNull(dictionary.put(1, -9));
        assertEquals(1, dictionary.size());

        assertEquals(-9, dictionary.put(1, 78));
        assertEquals(1, dictionary.size());

        assertEquals(78, dictionary.put(1, 6));
        assertEquals(1, dictionary.size());

        assertNull(dictionary.put(2, 17));
        assertEquals(2, dictionary.size());

        assertEquals(17, dictionary.put(2, 31));
        assertEquals(2, dictionary.size());

        assertEquals(31, dictionary.put(2, 42));
        assertEquals(2, dictionary.size());

        assertEquals(6, dictionary.put(1, null));
        assertEquals(2, dictionary.size());

        assertNull(dictionary.put(1, 0));
        assertEquals(2, dictionary.size());

        dictionary.remove(2);
        assertEquals(1, dictionary.size());

        assertNull(dictionary.put(2, 71));
        assertEquals(2, dictionary.size());

        dictionary.clear();
        assertEquals(0, dictionary.size());

        assertNull(dictionary.put(1, -52));
        assertEquals(1, dictionary.size());
    }

    @Test
    public void testGet() {
        Dictionary<Integer, Integer> dictionary = new Dictionary<>();

        assertNull(dictionary.get(1));

        dictionary.put(1, -9);
        assertEquals(-9, dictionary.get(1));
        assertEquals(-9, dictionary.get(1));

        dictionary.put(1, 78);
        assertEquals(78, dictionary.get(1));
        assertEquals(78, dictionary.get(1));

        assertNull(dictionary.get(2));

        dictionary.put(2, 17);
        assertEquals(17, dictionary.get(2));

        assertEquals(78, dictionary.get(1));

        dictionary.remove(2);

        assertNull(dictionary.get(2));

        dictionary.clear();

        assertNull(dictionary.get(1));
    }

    @Test
    void testRemove() {
        Dictionary<Integer, Integer> dictionary = new Dictionary<>();
        assertEquals(0, dictionary.size());

        dictionary.put(-1, null);
        dictionary.put(0, 1);
        dictionary.put(1, 1);
        dictionary.put(2, 2);
        dictionary.put(3, 3);
        dictionary.put(4, 5);
        dictionary.put(5, 8);
        dictionary.put(6, 13);
        dictionary.put(7, 21);
        assertEquals(9, dictionary.size());

        assertEquals(5, dictionary.remove(4));
        assertEquals(8, dictionary.size());

        assertNull(dictionary.get(-1));
        assertEquals(1, dictionary.get(0));
        assertEquals(1, dictionary.get(1));
        assertEquals(2, dictionary.get(2));
        assertEquals(3, dictionary.get(3));
        assertEquals(8, dictionary.get(5));
        assertEquals(13, dictionary.get(6));
        assertEquals(21, dictionary.get(7));

        assertNull(dictionary.remove(4));
        assertEquals(8, dictionary.size());

        assertNull(dictionary.remove(8));
        assertEquals(8, dictionary.size());

        assertNull(dictionary.remove(-1));
        assertEquals(7, dictionary.size());

        assertEquals(21, dictionary.remove(7));
        assertEquals(6, dictionary.size());

        assertEquals(1, dictionary.get(0));
        assertEquals(1, dictionary.get(1));
        assertEquals(2, dictionary.get(2));
        assertEquals(3, dictionary.get(3));
        assertEquals(8, dictionary.get(5));
        assertEquals(13, dictionary.get(6));
    }
}
