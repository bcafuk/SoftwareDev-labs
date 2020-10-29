package hr.fer.oprpp1.custom.collections.demo;

import hr.fer.oprpp1.custom.collections.SimpleHashtable;

/**
 * A demonstration of {@link SimpleHashtable}.
 */
public class SimpleHashTableDemo {
    /**
     * The contents of this method have been copied from
     * the assignment document written by doc. dr. sc. Marko Čupić.
     */
    public static void main(String[] args) {
        // create collection:
        SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);

        // fill data:
        examMarks.put("Ivana", 2);
        examMarks.put("Ante", 2);
        examMarks.put("Jasna", 2);
        examMarks.put("Kristina", 5);
        examMarks.put("Ivana", 5); // overwrites old grade for Ivana

        // query collection:
        Integer kristinaGrade = examMarks.get("Kristina");
        System.out.println("Kristina's exam grade is: " + kristinaGrade); // writes: 5

        // What is collection's size? Must be four!
        System.out.println("Number of stored pairs: " + examMarks.size()); // writes: 4

        for (SimpleHashtable.TableEntry<String, Integer> pair : examMarks) {
            System.out.printf("%s => %d%n", pair.getKey(), pair.getValue());
        }

        for (SimpleHashtable.TableEntry<String, Integer> pair1 : examMarks) {
            for (SimpleHashtable.TableEntry<String, Integer> pair2 : examMarks) {
                System.out.printf(
                        "(%s => %d) - (%s => %d)%n",
                        pair1.getKey(), pair1.getValue(),
                        pair2.getKey(), pair2.getValue()
                );
            }
        }
    }
}
