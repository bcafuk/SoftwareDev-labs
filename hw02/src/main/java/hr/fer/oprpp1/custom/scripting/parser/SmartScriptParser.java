package hr.fer.oprpp1.custom.scripting.parser;

import hr.fer.oprpp1.custom.collections.ArrayIndexedCollection;
import hr.fer.oprpp1.custom.collections.EmptyStackException;
import hr.fer.oprpp1.custom.collections.ObjectStack;
import hr.fer.oprpp1.custom.scripting.elems.*;
import hr.fer.oprpp1.custom.scripting.lexer.*;
import hr.fer.oprpp1.custom.scripting.nodes.*;

import java.util.Objects;

/**
 * A parser for the language defined in the assignment document.
 *
 * @author Borna Cafuk
 */
public class SmartScriptParser {
    /**
     * The lexer used to tokenize the input.
     */
    private SmartScriptLexer lexer;
    /**
     * The root node of the resulting document.
     */
    private DocumentNode documentNode;
    /**
     * The stack of nodes currently being processed.
     */
    private ObjectStack nodeStack = new ObjectStack();

    /**
     * Constructs a new parser and parses the given input.
     *
     * @param input the input to parse
     * @throws NullPointerException       if {@code input} is {@code null}.
     * @throws SmartScriptParserException if {@code input} is not syntactically valid.
     */
    public SmartScriptParser(String input) {
        Objects.requireNonNull(input, "The input must not be null.");

        lexer = new SmartScriptLexer(input);
        try {
            documentNode = new DocumentNode();
            nodeStack.push(documentNode);
            parse();
        } catch (SmartScriptLexerException e) {
            throw new SmartScriptParserException("Lexer exception: " + e.getMessage(), e);
        }
    }

    /**
     * Gets the root node.
     *
     * @return the root (document) node
     */
    public DocumentNode getDocumentNode() {
        return documentNode;
    }

    /**
     * Parses the input in its entirety.
     */
    private void parse() {
        while (lexer.nextToken().getType() != SmartScriptTokenType.EOF) {
            Node parent = (Node) nodeStack.peek();

            if (lexer.getToken().getType() == SmartScriptTokenType.BARE_STRING) {
                parent.addChildNode(new TextNode((String) lexer.getToken().getValue()));
                continue;
            }

            if (lexer.getToken().getType() == SmartScriptTokenType.TAG_LEFT) {
                lexer.setState(SmartScriptLexerState.TAG);
                parseTag();
                lexer.setState(SmartScriptLexerState.TEXT);
                continue;
            }

            throw new SmartScriptParserException("Unexpected " + lexer.getToken().getType() + " in text mode.");
        }

        if (nodeStack.size() != 1)
            throw new SmartScriptParserException("The input contains a tag which has not been closed.");
    }

    /**
     * Parses a tag.
     */
    private void parseTag() {
        Node parent = (Node) nodeStack.peek();
        SmartScriptToken tagName = lexer.nextToken();

        if (tagName.getType() == SmartScriptTokenType.EQUALS) {
            parent.addChildNode(parseEchoTag());
            return;
        }

        if (tagName.getType() == SmartScriptTokenType.IDENTIFIER) {
            String name = ((String) tagName.getValue()).toUpperCase();

            if (name.equals("END")) {
                if (lexer.nextToken().getType() != SmartScriptTokenType.TAG_RIGHT)
                    throw new SmartScriptParserException("Unexpected parameters in END tag.");

                nodeStack.pop();
                if (nodeStack.isEmpty())
                    throw new SmartScriptParserException("Unexpected END tag at top level.");
                return;
            }

            if (name.equals("FOR")) {
                ForLoopNode child = parseForTag();
                parent.addChildNode(child);
                nodeStack.push(child);
                return;
            }

            throw new SmartScriptParserException("Unknown tag type " + name);
        }

        throw new SmartScriptParserException("Invalid tag name " + tagName);
    }

    /**
     * Parses an echo ({@code =}) tag.
     *
     * @return the parsed node
     */
    private EchoNode parseEchoTag() {
        ArrayIndexedCollection elements = new ArrayIndexedCollection();

        while (lexer.nextToken().getType() != SmartScriptTokenType.TAG_RIGHT)
            elements.add(parseElement(lexer.getToken()));

        Element[] elementArray = new Element[elements.size()];

        for (int i = 0; i < elements.size(); i++)
            elementArray[i] = (Element) elements.get(i);

        return new EchoNode(elementArray);
    }

    /**
     * Parses an opening {@code FOR} tag.
     *
     * @return the parsed node
     */
    private ForLoopNode parseForTag() {
        if (lexer.nextToken().getType() != SmartScriptTokenType.IDENTIFIER)
            throw new SmartScriptParserException("The first parameter of a for loop must be a variable, got " + lexer.getToken().getType() + "instead.");

        ElementVariable variable = new ElementVariable((String) lexer.getToken().getValue());

        Element startExpression = parseElement(lexer.nextToken());
        if (!isValidForElement(startExpression))
            throw new SmartScriptParserException("The second parameter of a for-loop must be a variable, number or string, got " + lexer.getToken().getType() + "instead.");

        Element endExpression = parseElement(lexer.nextToken());
        if (!isValidForElement(endExpression))
            throw new SmartScriptParserException("The third parameter of a for-loop must be a variable, number or string, got " + lexer.getToken().getType() + "instead.");

        SmartScriptToken stepToken = lexer.nextToken();
        Element step = null;
        if (stepToken.getType() != SmartScriptTokenType.TAG_RIGHT) {
            step = parseElement(stepToken);
            if (!isValidForElement(step))
                throw new SmartScriptParserException("The second parameter of a for-loop must be a variable, number or string, got " + lexer.getToken().getType() + "instead.");

            if (lexer.nextToken().getType() != SmartScriptTokenType.TAG_RIGHT) {
                throw new SmartScriptLexerException("Too many parameters in for-loop.");
            }
        }

        return new ForLoopNode(variable, startExpression, endExpression, step);
    }

    /**
     * Parses a token into an element
     *
     * @param token the token to parse
     * @return the resulting element
     */
    private static Element parseElement(SmartScriptToken token) {
        return switch (token.getType()) {
            case IDENTIFIER -> new ElementVariable((String) token.getValue());
            case DOUBLE -> new ElementConstantDouble((Double) token.getValue());
            case INTEGER -> new ElementConstantInteger((Integer) token.getValue());
            case STRING -> new ElementString((String) token.getValue());
            case FUNCTION -> new ElementFunction((String) token.getValue());
            case OPERATOR -> new ElementOperator(Character.toString((Character) token.getValue()));
            default -> throw new SmartScriptParserException("The expected a valid element, got " + token.getType() + "instead.");
        };
    }

    /**
     * Checks if an element is allowed to be a start expression, end expression or a step in a {@code FOR} tag.
     *
     * @param element the element to check
     * @return {@code true} if the element is allowed, {@code false} otherwise
     */
    private static boolean isValidForElement(Element element) {
        return element instanceof ElementVariable ||
                element instanceof ElementConstantDouble ||
                element instanceof ElementConstantInteger ||
                element instanceof ElementString;
    }

    @Override
    public String toString() {
        return documentNode.toString();
    }
}
