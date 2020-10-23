package hr.fer.oprpp1.custom.collections.demo;

import hr.fer.oprpp1.custom.collections.ArrayIndexedCollection;
import hr.fer.oprpp1.custom.collections.Collection;
import hr.fer.oprpp1.custom.collections.LinkedListIndexedCollection;
import hr.fer.oprpp1.custom.collections.Tester;

/**
 * A demonstration of {@link Tester} and its use, {@link Collection#addAllSatisfying(Collection, Tester)}.
 */
public class TesterDemos {
    /**
     * The contents of this class have been copied verbatim from
     * the assignment document written by doc. dr. sc. Marko Čupić.
     */
    private static class EvenIntegerTester implements Tester {
        public boolean test(Object obj) {
            if(!(obj instanceof Integer)) return false;
            Integer i = (Integer)obj;
            return i % 2 == 0;
        }
    }

    /**
     * The contents of this method have been copied verbatim from
     * the assignment document written by doc. dr. sc. Marko Čupić.
     */
    public static void plainTester() {

        Tester t = new EvenIntegerTester();

        System.out.println(t.test("Ivo"));
        System.out.println(t.test(22));
        System.out.println(t.test(3));
    }

    /**
     * The contents of this method have been copied verbatim from
     * the assignment document written by doc. dr. sc. Marko Čupić.
     */
    public static void addAllSatisfying() {
        Collection col1 = new LinkedListIndexedCollection();
        Collection col2 = new ArrayIndexedCollection();

        col1.add(2);
        col1.add(3);
        col1.add(4);
        col1.add(5);
        col1.add(6);

        col2.add(12);
        col2.addAllSatisfying(col1, new EvenIntegerTester());

        col2.forEach(System.out::println);
    }

    /**
     * Calls the other demonstration methods:
     *
     * <ul>
     *     <li>{@link #plainTester()}</li>
     *     <li>{@link #addAllSatisfying()}</li>
     * </ul>
     */
    public static void main(String[] args) {
        System.out.println("Demo 1:");
        plainTester();

        System.out.println("\nDemo 2:");
        addAllSatisfying();
    }
}
