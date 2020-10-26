package hr.fer.oprpp1.custom.scripting.parser;

import hr.fer.oprpp1.custom.scripting.nodes.DocumentNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SmartScriptParserTest {
    @Test
    public void testNullInput() {
        assertThrows(NullPointerException.class, () -> new SmartScriptParser(null));
    }

    @Test
    public void testEmptyInput() {
        assertParsesSuccessfully("");
    }

    @Test
    public void testJustText() {
        String source = """
                Just text, no tags.
                \\\\ \\{$ { $
                Multiple lines, though.""";

        assertParsesSuccessfully(source);
    }

    @Test
    public void testEmptyEcho() {
        String source = "{$=$}";

        assertParsesSuccessfully(source);
    }

    @Test
    public void testComplexEcho() {
        String source = """
                {$     =     FOR  END i      i *     @sin "\\n
                \\r
                \\t\\"\\\\" 0.5-4+"Hello"-@fn$}
                """;

        assertParsesSuccessfully(source);
    }

    @Test
    public void testEmptyFors() {
        String source = """
                {$ FOR i i i i $}{$END$}
                {$   FOR    i 1a"o" $}{$         END $}
                """;

        assertParsesSuccessfully(source);
    }

    @Test
    public void testNestedFors() {
        String source = """
                Complex numbers:
                {$ FOR phi 0.0 3.14 0.1 $}
                  {$ FOR r 0.0 5.0 0.5 $}
                    {$= r $} cis({$= phi $}) = {$= r phi @cos * "0.000" @decfmt $} + {$= r phi @sin * "0.000" @decfmt $}i
                  {$ END $}
                {$ END $}
                """;

        assertParsesSuccessfully(source);
    }

    @Test
    public void testSample() {
        String source = """
                This is sample text.
                {$ FOR i 1 10 1 $}
                  This is {$= i $}-th time this message is generated.
                {$END$}
                {$FOR i 0.0 10.0 2.0 $}
                  sin({$=i$}^2) = {$= i i * @sin "0.000" @decfmt $}
                {$END$}
                """;

        assertParsesSuccessfully(source);
    }

    @Test
    public void testInvalidEscapeInText() {
        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser("\\\""));
    }

    @Test
    public void testInvalidEscapeInString() {
        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser("{$=\"\\{\"$}"));
    }

    @Test
    public void testUnclosedTag() {
        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser("{$= i 1 2"));
        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser("{$FOR i 1 2"));
    }

    @Test
    public void testInvalidTagName() {
        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser("{$5.0$}"));
        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser("{$TAG$}"));
    }

    @Test
    public void testInvalidOperator() {
        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser("{$= %$}"));
        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser("{$= .5$}"));
    }

    @Test
    public void testForWithTooFewParams() {
        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser("{$FOR i 1$}"));
    }

    @Test
    public void testForWithTooManyParams() {
        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser("{$FOR i 1 10 1 1$}"));
    }

    @Test
    public void testForWithWrongParam() {
        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser("{$FOR 1$}"));
        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser("{$FOR *$}"));
        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser("{$FOR @foo$}"));
        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser("{$FOR i @foo$}"));
        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser("{$FOR i *$}"));
        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser("{$FOR i 1 @foo$}"));
        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser("{$FOR i 1 *$}"));
        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser("{$FOR i 1 10 @foo$}"));
        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser("{$FOR i 1 10 *$}"));
    }

    @Test
    public void testEndWithParam() {
        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser("{$FOR i 1 10$}{$END 1$}"));
    }

    @Test
    public void testTooManyEnds() {
        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser("{$FOR i 1 10$}{$END$}{$END$}"));
    }

    @Test
    public void testTooFewEnds() {
        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser("{$FOR i 1 10$}"));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 6, 7})
    public void testExtraSuccessful(int testNumber) {
        String source = readExample(testNumber);

        assertParsesSuccessfully(source);
    }

    @ParameterizedTest
    @ValueSource(ints = {4, 5, 8, 9})
    public void testExtraThrowing(int testNumber) {
        String source = readExample(testNumber);

        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(source));
    }

    private static void assertParsesSuccessfully(String source) {
        SmartScriptParser parser = new SmartScriptParser(source);
        DocumentNode document = parser.getDocumentNode();
        String originalDocumentBody = document.toString();

        SmartScriptParser parser2 = new SmartScriptParser(originalDocumentBody);
        DocumentNode document2 = parser2.getDocumentNode();

        assertEquals(document, document2);
    }

    private String readExample(int n) {
        String filename = "extra/example" + n + ".txt";

        try (InputStream is = this.getClass().getClassLoader().getResourceAsStream(filename)) {
            if (is == null) throw new RuntimeException("The file " + filename + " is not available.");
            byte[] data = this.getClass().getClassLoader().getResourceAsStream(filename).readAllBytes();
            return new String(data, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            throw new RuntimeException("Error while reading the file.", ex);
        }
    }
}
