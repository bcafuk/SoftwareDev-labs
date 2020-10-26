package hr.fer.oprpp1.hw02;

import hr.fer.oprpp1.custom.scripting.nodes.DocumentNode;
import hr.fer.oprpp1.custom.scripting.parser.SmartScriptParser;
import hr.fer.oprpp1.custom.scripting.parser.SmartScriptParserException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * A class to test {@link SmartScriptParser}.
 * This is based on classes written by doc. dr. sc. Marko Čupić.
 */
public class SmartScriptTester {
    /**
     * Takes the path to a SmartScript source file as an argument and checks if the file parses correctly.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("The program expects exactly one argument!");
            System.exit(-1);
        }
        String filepath = args[0];

        String docBody = null;
        try {
            docBody = Files.readString(Paths.get(filepath));
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            System.exit(-1);
        }
        SmartScriptParser parser = null;
        try {
            parser = new SmartScriptParser(docBody);
        } catch (SmartScriptParserException e) {
            System.out.println("Unable to parse document: " + e.getMessage());
            System.exit(-1);
        } catch(Exception e) {
            System.out.println("If this line ever executes, you have failed this class!");
            System.exit(-1);
        }
        DocumentNode document = parser.getDocumentNode();
        String originalDocumentBody = document.toString();
        System.out.println(originalDocumentBody);
        SmartScriptParser parser2 = new SmartScriptParser(originalDocumentBody);
        DocumentNode document2 = parser2.getDocumentNode();
        boolean same = document.equals(document2); // ==> "same" must be true
        if (same) {
            System.out.println("The input and output are equal.");
        } else {
            System.out.println("The input and output differ:");
            System.out.println(document.toString());
            System.out.println("============================");
            System.out.println(document2.toString());
        }
    }
}
