package hr.fer.oprpp1.custom.collections.demo;

import hr.fer.oprpp1.custom.collections.ArrayIndexedCollection;
import hr.fer.oprpp1.custom.collections.Collection;
import hr.fer.oprpp1.custom.collections.ElementsGetter;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

/**
 * A class to demonstrate the use and behavior of {@link ElementsGetter}s.
 */
public class ElementsGetterDemos {
    /**
     * Demonstrates the basic use of {@link ElementsGetter}.
     * <p>
     * The contents of this method have been copied verbatim from
     * the assignment document written by doc. dr. sc. Marko Čupić.
     */
    public static void basicDemo() {
        Collection col = new ArrayIndexedCollection();
        col.add("Ivo");
        col.add("Ana");
        col.add("Jasna");

        ElementsGetter getter = col.createElementsGetter();

        System.out.println("Ima nepredanih elemenata: " + getter.hasNextElement());
        System.out.println("Jedan element: " + getter.getNextElement());
        System.out.println("Ima nepredanih elemenata: " + getter.hasNextElement());
        System.out.println("Jedan element: " + getter.getNextElement());
        System.out.println("Ima nepredanih elemenata: " + getter.hasNextElement());
        System.out.println("Jedan element: " + getter.getNextElement());
        System.out.println("Ima nepredanih elemenata: " + getter.hasNextElement());
        System.out.println("Jedan element: " + getter.getNextElement());  // Should throw
    }

    /**
     * Demonstrates the idempotency of {@link ElementsGetter#hasNextElement()}.
     * <p>
     * The contents of this method have been copied verbatim from
     * the assignment document written by doc. dr. sc. Marko Čupić.
     */
    public static void multipleHasNextElements() {
        Collection col = new ArrayIndexedCollection();
        col.add("Ivo");
        col.add("Ana");
        col.add("Jasna");

        ElementsGetter getter = col.createElementsGetter();

        System.out.println("Ima nepredanih elemenata: " + getter.hasNextElement());
        System.out.println("Ima nepredanih elemenata: " + getter.hasNextElement());
        System.out.println("Ima nepredanih elemenata: " + getter.hasNextElement());
        System.out.println("Ima nepredanih elemenata: " + getter.hasNextElement());
        System.out.println("Ima nepredanih elemenata: " + getter.hasNextElement());
        System.out.println("Jedan element: " + getter.getNextElement());

        System.out.println("Ima nepredanih elemenata: " + getter.hasNextElement());
        System.out.println("Ima nepredanih elemenata: " + getter.hasNextElement());
        System.out.println("Jedan element: " + getter.getNextElement());

        System.out.println("Ima nepredanih elemenata: " + getter.hasNextElement());
        System.out.println("Ima nepredanih elemenata: " + getter.hasNextElement());
        System.out.println("Ima nepredanih elemenata: " + getter.hasNextElement());
        System.out.println("Jedan element: " + getter.getNextElement());

        System.out.println("Ima nepredanih elemenata: " + getter.hasNextElement());
        System.out.println("Ima nepredanih elemenata: " + getter.hasNextElement());
    }

    /**
     * Demonstrates that {@link ElementsGetter#getNextElement()} works without {@link ElementsGetter#hasNextElement()}.
     * <p>
     * The contents of this method have been copied verbatim from
     * the assignment document written by doc. dr. sc. Marko Čupić.
     */
    public static void noHasNextElement() {
        Collection col = new ArrayIndexedCollection();
        col.add("Ivo");
        col.add("Ana");
        col.add("Jasna");

        ElementsGetter getter = col.createElementsGetter();

        System.out.println("Jedan element: " + getter.getNextElement());
        System.out.println("Jedan element: " + getter.getNextElement());
        System.out.println("Jedan element: " + getter.getNextElement());
        System.out.println("Jedan element: " + getter.getNextElement());  // Should throw
    }


    /**
     * Demonstrates that multiple {@link ElementsGetter}s can be made for the same {@link Collection}.
     * <p>
     * The contents of this method have been copied verbatim from
     * the assignment document written by doc. dr. sc. Marko Čupić.
     */
    public static void multipleGetters() {
        Collection col = new ArrayIndexedCollection();
        col.add("Ivo");
        col.add("Ana");
        col.add("Jasna");

        ElementsGetter getter1 = col.createElementsGetter();
        ElementsGetter getter2 = col.createElementsGetter();

        System.out.println("Jedan element: " + getter1.getNextElement());
        System.out.println("Jedan element: " + getter1.getNextElement());
        System.out.println("Jedan element: " + getter2.getNextElement());
        System.out.println("Jedan element: " + getter1.getNextElement());
        System.out.println("Jedan element: " + getter2.getNextElement());
    }

    /**
     * Demonstrates that {@link ElementsGetter} detects concurrent modification
     * and throws a {@link ConcurrentModificationException}.
     */
    public static void concurrentModification() {
        Collection col = new ArrayIndexedCollection();
        col.add("Ivo");
        col.add("Ana");
        col.add("Jasna");

        ElementsGetter getter = col.createElementsGetter();

        System.out.println("Jedan element: " + getter.getNextElement());
        System.out.println("Jedan element: " + getter.getNextElement());

        col.clear();

        System.out.println("Jedan element: " + getter.getNextElement()); // Should throw
    }

    /**
     * Calls the other demonstration methods:
     *
     * <ul>
     *     <li>{@link #basicDemo()}</li>
     *     <li>{@link #multipleHasNextElements()}</li>
     *     <li>{@link #noHasNextElement()}</li>
     *     <li>{@link #multipleGetters()}</li>
     *     <li>{@link #concurrentModification()}</li>
     * </ul>
     *
     * @param args command line arguments to the program; ignored
     */
    public static void main(String[] args) {
        System.out.println("Demo 1:");
        try {
            basicDemo();
        } catch (NoSuchElementException e) {
            System.out.println(e.toString());
        }

        System.out.println("\nDemo 2:");
        multipleHasNextElements();

        System.out.println("\nDemo 3:");
        try {
            noHasNextElement();
        } catch (NoSuchElementException e) {
            System.out.println(e.toString());
        }

        System.out.println("\nDemo 4:");
        multipleGetters();

        System.out.println("\nDemo 5:");
        try {
            concurrentModification();
        } catch (ConcurrentModificationException e) {
            System.out.println(e.toString());
        }
    }
}
