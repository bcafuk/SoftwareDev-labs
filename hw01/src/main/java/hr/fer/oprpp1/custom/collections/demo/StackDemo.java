package hr.fer.oprpp1.custom.collections.demo;

import hr.fer.oprpp1.custom.collections.ObjectStack;

import java.util.regex.Pattern;

/**
 * A demonstration of {@link ObjectStack}.
 *
 * @author Borna Cafuk
 */
public class StackDemo {
    /**
     * Prints the given reason to {@code stderr} and exits the program with status code 1.
     *
     * @param reason the reason for quitting the program
     */
    private static void exitProgram(String reason) {
        System.err.println(reason);
        System.exit(1);
    }

    /**
     * Computes the value of a mathematical expression using integers in postfix notation.
     * <p>
     * Reads a single command-line parameter: the expression in the form of a space-separated list of elements.
     *
     * @param args command-line argumnets
     */
    public static void main(String[] args) {
        if (args.length != 1)
            exitProgram("This program requires exactly one argument.");

        ObjectStack stack = new ObjectStack();
        String[] elements = args[0].split(" ");
        Pattern numberPattern = Pattern.compile("[-+]?\\d+");

        for (String element : elements) {
            if (numberPattern.matcher(element).matches()) {
                stack.push(Integer.parseInt(element));
                continue;
            }

            if (stack.size() < 2)
                exitProgram("Invalid expression: not enough operands.");

            Integer[] operands = new Integer[]{(Integer) stack.pop(), (Integer) stack.pop()};

            switch (element) {
                case "+" -> stack.push(operands[1] + operands[0]);
                case "-" -> stack.push(operands[1] - operands[0]);
                case "*" -> stack.push(operands[1] * operands[0]);
                case "/" -> {
                    if (operands[0] == 0)
                        exitProgram("Division by zero is not allowed.");
                    stack.push(operands[1] / operands[0]);
                }
                default -> exitProgram("Unknown operand: " + element);
            }
        }

        if (stack.size() != 1)
            exitProgram("Expected a stack of size 1 at the end of execution, got a stack of size " + stack.size() + ".");

        System.out.println(stack.pop());
    }
}
