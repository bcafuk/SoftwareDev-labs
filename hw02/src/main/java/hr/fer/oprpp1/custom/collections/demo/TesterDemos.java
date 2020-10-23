package hr.fer.oprpp1.custom.collections.demo;

import hr.fer.oprpp1.custom.collections.Tester;

/**
 * A demonstration of {@link Tester}.
 */
public class TesterDemos {
    /**
     * The contents of this method have been copied verbatim from
     * the assignment document written by doc. dr. sc. Marko Čupić.
     */
    public static void plainTester() {
        class EvenIntegerTester implements Tester {
            public boolean test(Object obj) {
                if(!(obj instanceof Integer)) return false;
                Integer i = (Integer)obj;
                return i % 2 == 0;
            }
        }

        Tester t = new EvenIntegerTester();

        System.out.println(t.test("Ivo"));
        System.out.println(t.test(22));
        System.out.println(t.test(3));
    }

    /**
     * Calls the other demonstration methods:
     *
     * <ul>
     *     <li>{@link #plainTester()}</li>
     * </ul>
     */
    public static void main(String[] args) {
        System.out.println("Demo 1:");
        plainTester();
    }
}
