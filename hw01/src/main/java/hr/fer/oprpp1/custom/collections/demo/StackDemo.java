package hr.fer.oprpp1.custom.collections.demo;

import hr.fer.oprpp1.custom.collections.ObjectStack;

import java.util.regex.Pattern;

public class StackDemo {
    private static void exitProgram(String reason) {
        System.err.println(reason);
        System.exit(1);
    }

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
