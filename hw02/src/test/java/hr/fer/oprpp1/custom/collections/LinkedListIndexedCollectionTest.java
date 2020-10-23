package hr.fer.oprpp1.custom.collections;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LinkedListIndexedCollectionTest {
    private static class Element {
        public int comparisonID;
        public int uniqueID;

        public Element(int comparisonID, int uniqueID) {
            this.comparisonID = comparisonID;
            this.uniqueID = uniqueID;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Element)) return false;
            Element element = (Element) o;
            return comparisonID == element.comparisonID;
        }
    }

    @Test
    public void defaultConstructor() {
        LinkedListIndexedCollection collection = new LinkedListIndexedCollection();
        assertTrue(collection.isEmpty());
        assertEquals(0, collection.size());
    }

    @Test
    public void addNullElement() {
        LinkedListIndexedCollection collection = new LinkedListIndexedCollection();

        assertThrows(
                NullPointerException.class,
                () -> collection.add(null)
        );
    }

    @Test
    public void addElement() {
        LinkedListIndexedCollection collection = new LinkedListIndexedCollection();

        Element element = new Element(0, 0);
        collection.add(element);

        assertFalse(collection.isEmpty());
        assertEquals(1, collection.size());

        Element elementGotten = (Element) collection.get(0);
        assertEquals(element.uniqueID, elementGotten.uniqueID);
        assertEquals(element.comparisonID, elementGotten.comparisonID);
    }

    private LinkedListIndexedCollection createCollection(int elementCount) {
        LinkedListIndexedCollection collection = new LinkedListIndexedCollection();

        for (int i = 0; i < elementCount; i++)
            collection.add(new Element(-i, i));

        return collection;
    }

    @Test
    public void addAndGetMultipleElements() {
        final int elementCount = 100;
        LinkedListIndexedCollection collection = createCollection(elementCount);

        assertEquals(elementCount, collection.size());

        for (int i = 0; i < elementCount; i++) {
            Element elementGotten = (Element) collection.get(i);
            assertEquals(i, elementGotten.uniqueID);
            assertEquals(-i, elementGotten.comparisonID);
        }
    }

    @Test
    public void addDuplicates() {
        LinkedListIndexedCollection collection = new LinkedListIndexedCollection();

        for (int i = 0; i < 10; i++)
            collection.add(new Element(1, 1));

        for (int i = 0; i < 10; i++) {
            Element elementGotten = (Element) collection.get(i);
            assertEquals(1, elementGotten.uniqueID);
            assertEquals(1, elementGotten.comparisonID);
        }
    }

    @Test
    public void containsElement() {
        LinkedListIndexedCollection collection = new LinkedListIndexedCollection();

        collection.add(new Element(0, 0));
        assertTrue(collection.contains(new Element(0, 1)));
    }

    @Test
    public void doesNotContainElement() {
        LinkedListIndexedCollection collection = new LinkedListIndexedCollection();

        collection.add(new Element(0, 0));
        assertFalse(collection.contains(new Element(1, 1)));
        assertFalse(collection.contains(null));
    }

    @Test
    public void indexOfElement() {
        final int elementCount = 100;
        final int findIndex = 47;

        LinkedListIndexedCollection collection = createCollection(elementCount);

        assertEquals(findIndex, collection.indexOf(new Element(-findIndex, -1)));
    }

    @Test
    public void indexOfNonexistentElement() {
        final int elementCount = 100;
        LinkedListIndexedCollection collection = createCollection(elementCount);

        assertEquals(-1, collection.indexOf(new Element(1, 1)));
        assertEquals(-1, collection.indexOf(null));
    }

    @Test
    public void indexOfDuplicateElement() {
        LinkedListIndexedCollection collection = new LinkedListIndexedCollection();

        for (int i = 0; i < 10; i++)
            collection.add(new Element(-i, i));

        collection.add(new Element(1, -1));

        for (int i = 10; i < 20; i++)
            collection.add(new Element(-i, i));

        collection.add(new Element(1, -2));

        for (int i = 20; i < 30; i++)
            collection.add(new Element(-i, i));

        assertEquals(10, collection.indexOf(new Element(1, -2)));
    }

    @Test
    public void getOutOfBounds() {
        final int elementCount = 100;
        LinkedListIndexedCollection collection = createCollection(elementCount);

        assertThrows(
                IndexOutOfBoundsException.class,
                () -> collection.get(-1)
        );
        assertThrows(
                IndexOutOfBoundsException.class,
                () -> collection.get(elementCount)
        );
    }

    private void testRemoval(int elementCount, int removalIndex) {
        LinkedListIndexedCollection collection = createCollection(elementCount);

        collection.remove(removalIndex);

        assertEquals(elementCount - 1, collection.size());

        for (int i = 0; i < removalIndex; i++) {
            Element elementGotten = (Element) collection.get(i);
            assertEquals(i, elementGotten.uniqueID);
            assertEquals(-i, elementGotten.comparisonID);
        }

        for (int i = removalIndex; i < elementCount - 1; i++) {
            Element elementGotten = (Element) collection.get(i);
            assertEquals(i + 1, elementGotten.uniqueID);
            assertEquals(-i - 1, elementGotten.comparisonID);
        }
    }

    @Test
    public void removeByIndex() {testRemoval(100, 47);}

    @Test
    public void removeFromBeginning() {testRemoval(100, 0);}

    @Test
    public void removeFromEnd() {testRemoval(100, 99);}

    @Test
    public void removeOnlyElement() {
        LinkedListIndexedCollection collection = new LinkedListIndexedCollection();

        collection.add(new Element(0, 0));
        collection.remove(0);

        assertTrue(collection.isEmpty());
    }

    @Test
    public void removeDuplicateElement() {
        LinkedListIndexedCollection collection = new LinkedListIndexedCollection();

        for (int i = 0; i < 10; i++)
            collection.add(new Element(-i, i));

        collection.add(new Element(1, -1));

        for (int i = 10; i < 20; i++)
            collection.add(new Element(-i, i));

        collection.add(new Element(1, -2));

        for (int i = 20; i < 30; i++)
            collection.add(new Element(-i, i));

        assertTrue(collection.remove(new Element(1, -2)));

        assertEquals(31, collection.size());

        for (int i = 0; i < 20; i++) {
            Element elementGotten = (Element) collection.get(i);
            assertEquals(i, elementGotten.uniqueID);
            assertEquals(-i, elementGotten.comparisonID);
        }

        Element duplicateElement = (Element) collection.get(20);
        assertEquals(-2, duplicateElement.uniqueID);
        assertEquals(1, duplicateElement.comparisonID);

        for (int i = 20; i < 30; i++) {
            Element elementGotten = (Element) collection.get(i + 1);
            assertEquals(i, elementGotten.uniqueID);
            assertEquals(-i, elementGotten.comparisonID);
        }
    }

    @Test
    public void removeByOutOfBoundsIndex() {
        final int elementCount = 100;
        LinkedListIndexedCollection collection = createCollection(elementCount);

        assertThrows(
                IndexOutOfBoundsException.class,
                () -> collection.remove(-1)
        );
        assertThrows(
                IndexOutOfBoundsException.class,
                () -> collection.remove(elementCount)
        );

        assertEquals(elementCount, collection.size());
    }

    @Test
    public void removeByReference() {
        final int elementCount = 100;
        final int removalID = 47;

        LinkedListIndexedCollection collection = createCollection(elementCount);

        assertTrue(collection.remove(new Element(-removalID, -1)));

        assertEquals(elementCount - 1, collection.size());

        for (int i = 0; i < removalID; i++) {
            Element elementGotten = (Element) collection.get(i);
            assertEquals(i, elementGotten.uniqueID);
            assertEquals(-i, elementGotten.comparisonID);
        }

        for (int i = removalID; i < elementCount - 1; i++) {
            Element elementGotten = (Element) collection.get(i);
            assertEquals(i + 1, elementGotten.uniqueID);
            assertEquals(-i - 1, elementGotten.comparisonID);
        }
    }

    @Test
    public void removeByNonexistentReference() {
        final int elementCount = 100;
        LinkedListIndexedCollection collection = createCollection(elementCount);

        assertFalse(collection.remove(new Element(1, 1)));

        assertEquals(elementCount, collection.size());
    }

    private void testInsertion(int elementCount, int insertionIndex) {
        LinkedListIndexedCollection collection = createCollection(elementCount);

        collection.insert(new Element(1, -1), insertionIndex);

        assertEquals(elementCount + 1, collection.size());

        for (int i = 0; i < insertionIndex; i++) {
            Element elementGotten = (Element) collection.get(i);
            assertEquals(i, elementGotten.uniqueID);
            assertEquals(-i, elementGotten.comparisonID);
        }

        Element insertedGotten = (Element) collection.get(insertionIndex);
        assertEquals(-1, insertedGotten.uniqueID);
        assertEquals(1, insertedGotten.comparisonID);

        for (int i = insertionIndex + 1; i < elementCount + 1; i++) {
            Element elementGotten = (Element) collection.get(i);
            assertEquals(i - 1, elementGotten.uniqueID);
            assertEquals(-i + 1, elementGotten.comparisonID);
        }
    }

    @Test
    public void insertElement() {testInsertion(100, 47);}

    @Test
    public void insertElementAtBeginning() {testInsertion(100, 0);}

    @Test
    public void insertElementAtEnd() {testInsertion(100, 100);}

    @Test
    public void insertElementOutOfBounds() {
        final int elementCount = 100;
        LinkedListIndexedCollection collection = createCollection(elementCount);

        assertThrows(
                IndexOutOfBoundsException.class,
                () -> collection.insert(new Element(0, 0), -1)
        );
        assertThrows(
                IndexOutOfBoundsException.class,
                () -> collection.insert(new Element(0, 0), elementCount + 1)
        );

        assertEquals(elementCount, collection.size());
    }

    @Test
    public void clear() {
        final int elementCount = 100;
        LinkedListIndexedCollection collection = createCollection(elementCount);

        assertFalse(collection.isEmpty());

        collection.clear();

        assertTrue(collection.isEmpty());
        assertEquals(0, collection.size());
    }

    @Test
    public void addAfterClear() {
        final int elementCount = 100;
        LinkedListIndexedCollection collection = createCollection(elementCount);

        collection.clear();

        for (int i = 0; i < elementCount; i++)
            collection.add(new Element(i, -i));

        assertEquals(elementCount, collection.size());

        for (int i = 0; i < elementCount; i++) {
            Element elementGotten = (Element) collection.get(i);
            assertEquals(-i, elementGotten.uniqueID);
            assertEquals(i, elementGotten.comparisonID);
        }
    }

    @Test
    public void convertToArray() {
        final int elementCount = 100;
        LinkedListIndexedCollection collection = createCollection(elementCount);

        Object[] array = collection.toArray();

        assertEquals(elementCount, array.length);
        for (int i = 0; i < elementCount; i++) {
            Element elementGotten = (Element) array[i];
            assertEquals(i, elementGotten.uniqueID);
            assertEquals(-i, elementGotten.comparisonID);
        }
    }

    @Test
    public void forEach() {
        final int elementCount = 100;
        LinkedListIndexedCollection collection = createCollection(elementCount);

        class CheckProcessor extends Processor {
            public int lastVisited = -1;

            public void process(Object value) {
                Element element = (Element) value;
                assertEquals(lastVisited + 1, element.uniqueID);
                assertEquals(-lastVisited - 1, element.comparisonID);
                lastVisited = element.uniqueID;
            }
        }

        CheckProcessor processor = new CheckProcessor();
        collection.forEach(processor);
        assertEquals(elementCount - 1, processor.lastVisited);
    }

    @Test
    public void addAll() {
        final int elementCount = 100;
        LinkedListIndexedCollection source = createCollection(elementCount);

        LinkedListIndexedCollection collection = new LinkedListIndexedCollection();
        collection.addAll(source);

        assertEquals(elementCount, collection.size());
        assertArrayEquals(source.toArray(), collection.toArray());
    }

    @Test
    public void copyConstructor() {
        final int elementCount = 100;
        LinkedListIndexedCollection source = createCollection(elementCount);

        LinkedListIndexedCollection collection = new LinkedListIndexedCollection(source);

        assertEquals(elementCount, collection.size());
        assertArrayEquals(source.toArray(), collection.toArray());
    }

    @Test
    public void copyConstructorWithNull() {
        assertThrows(
                NullPointerException.class,
                () -> new LinkedListIndexedCollection(null)
        );
    }
}
