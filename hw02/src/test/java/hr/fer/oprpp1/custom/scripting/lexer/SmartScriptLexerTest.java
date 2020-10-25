package hr.fer.oprpp1.custom.scripting.lexer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The contents of this class have been adapred from {@link hr.fer.oprpp1.hw02.prob1.Prob1Test},
 * which was written by doc. dr. sc. Marko Čupić.
 */
class SmartScriptLexerTest {
    @Test
    public void testNotNull() {
        SmartScriptLexer lexer = new SmartScriptLexer("");

        assertNotNull(lexer.nextToken(), "Token was expected but null was returned.");
    }

    @Test
    public void testNullInput() {
        // must throw!
        assertThrows(NullPointerException.class, () -> new SmartScriptLexer(null));
    }

    @Test
    public void testEmpty() {
        SmartScriptLexer lexer = new SmartScriptLexer("");

        assertEquals(SmartScriptTokenType.EOF, lexer.nextToken().getType(), "Empty input must generate only EOF token.");
    }

    @Test
    public void testGetReturnsLastNext() {
        // Calling getToken once or several times after calling nextToken must return what nextToken returned each time...
        SmartScriptLexer lexer = new SmartScriptLexer("");

        SmartScriptToken token = lexer.nextToken();
        assertEquals(token, lexer.getToken(), "getToken returned different token than nextToken.");
        assertEquals(token, lexer.getToken(), "getToken returned different token than nextToken.");
    }

    @Test
    public void testReadAfterEOF() {
        SmartScriptLexer lexer = new SmartScriptLexer("");

        // will obtain EOF
        lexer.nextToken();
        // will throw!
        assertThrows(SmartScriptLexerException.class, lexer::nextToken);
    }

    @Test
    public void testOnlyWhitespace() {
        String input = "   \r\n\t    ";
        // When the input is only made of spaces, tabs, newlines, etc...
        SmartScriptLexer lexer = new SmartScriptLexer(input);

        // We expect the following stream of tokens
        SmartScriptToken[] correctData = {
                new SmartScriptToken(SmartScriptTokenType.BARE_STRING, input),
                new SmartScriptToken(SmartScriptTokenType.EOF, null)
        };

        checkTokenStream(lexer, correctData);
    }

    @Test
    public void testBasicText() {
        String input = "  Štefanija\r\n\t Automobil   ";
        // Let's check for several words...
        SmartScriptLexer lexer = new SmartScriptLexer(input);

        // We expect the following stream of tokens
        SmartScriptToken[] correctData = {
                new SmartScriptToken(SmartScriptTokenType.BARE_STRING, input),
                new SmartScriptToken(SmartScriptTokenType.EOF, null)
        };

        checkTokenStream(lexer, correctData);
    }

    @Test
    public void testTagOpening() {
        // Let's check for a tag opening in the middle of words
        SmartScriptLexer lexer = new SmartScriptLexer("  text  {$ more text ");

        // We expect the following stream of tokens
        SmartScriptToken[] correctData = {
                new SmartScriptToken(SmartScriptTokenType.BARE_STRING, "  text  "),
                new SmartScriptToken(SmartScriptTokenType.TAG_LEFT, null),
                new SmartScriptToken(SmartScriptTokenType.BARE_STRING, " more text "),
                new SmartScriptToken(SmartScriptTokenType.EOF, null)
        };

        checkTokenStream(lexer, correctData);
    }

    @Test
    public void testLeftBrace() {
        // A left brace which is not part of a tag opening
        String input = "Hello { $ world!";
        SmartScriptLexer lexer = new SmartScriptLexer(input);

        // We expect the following stream of tokens
        SmartScriptToken[] correctData = {
                new SmartScriptToken(SmartScriptTokenType.BARE_STRING, input),
                new SmartScriptToken(SmartScriptTokenType.EOF, null)
        };

        checkTokenStream(lexer, correctData);
    }

    @Test
    public void testWordStartingWithEscape() {
        SmartScriptLexer lexer = new SmartScriptLexer("\\{$FOR$}");

        // We expect the following stream of tokens
        SmartScriptToken[] correctData = {
                new SmartScriptToken(SmartScriptTokenType.BARE_STRING, "{$FOR$}"),
                new SmartScriptToken(SmartScriptTokenType.EOF, null)
        };

        checkTokenStream(lexer, correctData);
    }

    @Test
    public void testInvalidEscapeEnding() {
        SmartScriptLexer lexer = new SmartScriptLexer("   \\");  // this is three spaces and a single backslash -- 4 letters string

        // will throw!
        assertThrows(SmartScriptLexerException.class, lexer::nextToken);
    }

    @Test
    public void testInvalidEscape() {
        SmartScriptLexer lexer = new SmartScriptLexer("   \\a    ");

        // will throw!
        assertThrows(SmartScriptLexerException.class, lexer::nextToken);
    }

    @Test
    public void testWordWithManyEscapes() {
        // Let's check for several escapes...
        SmartScriptLexer lexer = new SmartScriptLexer("Example { bla } blu \\{$=1$}. Nothing interesting {=here}.");

        // We expect the input to be just one bare string
        SmartScriptToken[] correctData = {
                new SmartScriptToken(SmartScriptTokenType.BARE_STRING, "Example { bla } blu {$=1$}. Nothing interesting {=here}."),
                new SmartScriptToken(SmartScriptTokenType.EOF, null)
        };

        checkTokenStream(lexer, correctData);
    }

    // Helper method for checking if lexer generates the same stream of tokens
    // as the given stream.
    private void checkTokenStream(SmartScriptLexer lexer, SmartScriptToken[] correctData) {
        int counter = 0;
        for (SmartScriptToken expected : correctData) {
            SmartScriptToken actual = lexer.nextToken();
            String msg = "Checking token " + counter + ":";
            assertEquals(expected.getType(), actual.getType(), msg);
            assertEquals(expected.getValue(), actual.getValue(), msg);
            counter++;
        }
    }

    @Test
    public void testNullState() {
        assertThrows(NullPointerException.class, () -> new SmartScriptLexer("").setState(null));
    }

    @Test
    public void testNotNullInTag() {
        SmartScriptLexer lexer = new SmartScriptLexer("");
        lexer.setState(SmartScriptLexerState.TAG);

        assertNotNull(lexer.nextToken(), "Token was expected but null was returned.");
    }

    @Test
    public void testEmptyInTag() {
        SmartScriptLexer lexer = new SmartScriptLexer("");
        lexer.setState(SmartScriptLexerState.TAG);

        assertEquals(SmartScriptTokenType.EOF, lexer.nextToken().getType(), "Empty input must generate only EOF token.");
    }

    @Test
    public void testGetReturnsLastNextInTag() {
        // Calling getToken once or several times after calling nextToken must return what nextToken returned each time...
        SmartScriptLexer lexer = new SmartScriptLexer("");
        lexer.setState(SmartScriptLexerState.TAG);

        SmartScriptToken token = lexer.nextToken();
        assertEquals(token, lexer.getToken(), "getToken returned different token than nextToken.");
        assertEquals(token, lexer.getToken(), "getToken returned different token than nextToken.");
    }

    @Test
    public void testReadAfterEOFInTag() {
        SmartScriptLexer lexer = new SmartScriptLexer("");
        lexer.setState(SmartScriptLexerState.TAG);

        // will obtain EOF
        lexer.nextToken();
        // will throw!
        assertThrows(SmartScriptLexerException.class, lexer::nextToken);
    }

    @Test
    public void testNoActualContentInTag() {
        // When input is only of spaces, tabs, newlines, etc...
        SmartScriptLexer lexer = new SmartScriptLexer("   \r\n\t    ");
        lexer.setState(SmartScriptLexerState.TAG);

        assertEquals(SmartScriptTokenType.EOF, lexer.nextToken().getType(), "Input had no content. SmartScriptLexer should generated only EOF token.");
    }

    @Test
    public void testOnlyWhitespaceInTag() {
        // Whitespace should get ignored
        SmartScriptLexer lexer = new SmartScriptLexer("  \r\n\t   ");
        lexer.setState(SmartScriptLexerState.TAG);

        assertEquals(SmartScriptTokenType.EOF, lexer.nextToken().getType(), "Input had only whitespace. SmartScriptLexer should generated only EOF token.");
    }

    @Test
    public void testTwoNumbers() {
        // Let's check for several numbers...
        SmartScriptLexer lexer = new SmartScriptLexer("  -1234\r\n\t 5678.5   ");
        lexer.setState(SmartScriptLexerState.TAG);

        SmartScriptToken[] correctData = {
                new SmartScriptToken(SmartScriptTokenType.NUMBER, -1234d),
                new SmartScriptToken(SmartScriptTokenType.NUMBER, 5678.5d),
                new SmartScriptToken(SmartScriptTokenType.EOF, null)
        };

        checkTokenStream(lexer, correctData);
    }

    @Test
    public void testDoublePeriodsNumber() {
        SmartScriptLexer lexer = new SmartScriptLexer("  -12.75.56   ");
        lexer.setState(SmartScriptLexerState.TAG);

        // Expect the number before the second period to be lexed correctly
        checkToken(lexer.nextToken(), new SmartScriptToken(SmartScriptTokenType.NUMBER, -12.75d));

        // will throw!
        assertThrows(SmartScriptLexerException.class, lexer::nextToken);
    }

    @Test
    public void testIdentifiers() {
        // Let's check for several identifiers...
        SmartScriptLexer lexer = new SmartScriptLexer("  i FOR\n pi_2 _   ");
        lexer.setState(SmartScriptLexerState.TAG);

        SmartScriptToken[] correctData = {
                new SmartScriptToken(SmartScriptTokenType.IDENTIFIER, "i"),
                new SmartScriptToken(SmartScriptTokenType.IDENTIFIER, "FOR"),
                new SmartScriptToken(SmartScriptTokenType.IDENTIFIER, "pi_2"),
                new SmartScriptToken(SmartScriptTokenType.IDENTIFIER, "_"),
                new SmartScriptToken(SmartScriptTokenType.EOF, null)
        };

        checkTokenStream(lexer, correctData);
    }

    @Test
    public void testFunctions() {
        // Let's check for several functions...
        SmartScriptLexer lexer = new SmartScriptLexer("  @i @FOR\n @pi_2@_   ");
        lexer.setState(SmartScriptLexerState.TAG);

        SmartScriptToken[] correctData = {
                new SmartScriptToken(SmartScriptTokenType.FUNCTION, "@i"),
                new SmartScriptToken(SmartScriptTokenType.FUNCTION, "@FOR"),
                new SmartScriptToken(SmartScriptTokenType.FUNCTION, "@pi_2"),
                new SmartScriptToken(SmartScriptTokenType.FUNCTION, "@_"),
                new SmartScriptToken(SmartScriptTokenType.EOF, null)
        };

        checkTokenStream(lexer, correctData);
    }

    @Test
    public void testTagClosing() {
        // Let's check for a closing tag
        SmartScriptLexer lexer = new SmartScriptLexer("ident$}5");
        lexer.setState(SmartScriptLexerState.TAG);

        // We expect the following stream of tokens
        SmartScriptToken[] correctData = {
                new SmartScriptToken(SmartScriptTokenType.IDENTIFIER, "ident"),
                new SmartScriptToken(SmartScriptTokenType.TAG_RIGHT, null),
                new SmartScriptToken(SmartScriptTokenType.NUMBER, 5d),
                new SmartScriptToken(SmartScriptTokenType.EOF, null)
        };

        checkTokenStream(lexer, correctData);
    }

    @Test
    public void testEquals() {
        // Let's check for an equals symbols
        SmartScriptLexer lexer = new SmartScriptLexer("  \n =  \n i i == =");
        lexer.setState(SmartScriptLexerState.TAG);

        // We expect the following stream of tokens
        SmartScriptToken[] correctData = {
                new SmartScriptToken(SmartScriptTokenType.EQUALS, null),
                new SmartScriptToken(SmartScriptTokenType.IDENTIFIER, "i"),
                new SmartScriptToken(SmartScriptTokenType.IDENTIFIER, "i"),
                new SmartScriptToken(SmartScriptTokenType.EQUALS, null),
                new SmartScriptToken(SmartScriptTokenType.EQUALS, null),
                new SmartScriptToken(SmartScriptTokenType.EQUALS, null),
                new SmartScriptToken(SmartScriptTokenType.EOF, null)
        };

        checkTokenStream(lexer, correctData);
    }

    @Test
    public void testOperators() {
        // Test operators
        SmartScriptLexer lexer = new SmartScriptLexer("  +-*/^ a+5/i-@foo\n-\n15");
        lexer.setState(SmartScriptLexerState.TAG);

        // We expect the following stream of tokens
        SmartScriptToken[] correctData = {
                new SmartScriptToken(SmartScriptTokenType.OPERATOR, '+'),
                new SmartScriptToken(SmartScriptTokenType.OPERATOR, '-'),
                new SmartScriptToken(SmartScriptTokenType.OPERATOR, '*'),
                new SmartScriptToken(SmartScriptTokenType.OPERATOR, '/'),
                new SmartScriptToken(SmartScriptTokenType.OPERATOR, '^'),
                new SmartScriptToken(SmartScriptTokenType.IDENTIFIER, "a"),
                new SmartScriptToken(SmartScriptTokenType.OPERATOR, '+'),
                new SmartScriptToken(SmartScriptTokenType.NUMBER, 5d),
                new SmartScriptToken(SmartScriptTokenType.OPERATOR, '/'),
                new SmartScriptToken(SmartScriptTokenType.IDENTIFIER, "i"),
                new SmartScriptToken(SmartScriptTokenType.OPERATOR, '-'),
                new SmartScriptToken(SmartScriptTokenType.FUNCTION, "@foo"),
                new SmartScriptToken(SmartScriptTokenType.OPERATOR, '-'),
                new SmartScriptToken(SmartScriptTokenType.NUMBER, 15d),
                new SmartScriptToken(SmartScriptTokenType.EOF, null)
        };

        checkTokenStream(lexer, correctData);
    }

    @Test
    public void testMinus() {
        // The minus character can both start a number and be an operator, this is tested here
        SmartScriptLexer lexer = new SmartScriptLexer("- 5-4-a-");
        lexer.setState(SmartScriptLexerState.TAG);

        // We expect the following stream of tokens
        SmartScriptToken[] correctData = {
                new SmartScriptToken(SmartScriptTokenType.OPERATOR, '-'),
                new SmartScriptToken(SmartScriptTokenType.NUMBER, 5d),
                new SmartScriptToken(SmartScriptTokenType.NUMBER, -4d),
                new SmartScriptToken(SmartScriptTokenType.OPERATOR, '-'),
                new SmartScriptToken(SmartScriptTokenType.IDENTIFIER, "a"),
                new SmartScriptToken(SmartScriptTokenType.OPERATOR, '-'),
                new SmartScriptToken(SmartScriptTokenType.EOF, null)
        };

        checkTokenStream(lexer, correctData);
    }

    @Test
    public void testStrings() {
        // Two simple strings
        SmartScriptLexer lexer = new SmartScriptLexer("    \"Hello, world!\"\"$}=@foo\"    ");
        lexer.setState(SmartScriptLexerState.TAG);

        // We expect the following stream of tokens
        SmartScriptToken[] correctData = {
                new SmartScriptToken(SmartScriptTokenType.STRING, "Hello, world!"),
                new SmartScriptToken(SmartScriptTokenType.STRING, "$}=@foo"),
                new SmartScriptToken(SmartScriptTokenType.EOF, null)
        };

        checkTokenStream(lexer, correctData);
    }

    @Test
    public void testStringEscapes() {
        // Two simple strings
        SmartScriptLexer lexer = new SmartScriptLexer("\"Joe \\\"Long\\\" Smith\" \"\\\\\\\"\" \"H\\ne\\tl\\rl\\\\o\\\"!\"");
        lexer.setState(SmartScriptLexerState.TAG);

        // We expect the following stream of tokens
        SmartScriptToken[] correctData = {
                new SmartScriptToken(SmartScriptTokenType.STRING, "Joe \"Long\" Smith"),
                new SmartScriptToken(SmartScriptTokenType.STRING, "\\\""),
                new SmartScriptToken(SmartScriptTokenType.STRING, "H\ne\tl\rl\\o\"!"),
                new SmartScriptToken(SmartScriptTokenType.EOF, null)
        };

        checkTokenStream(lexer, correctData);
    }

    @Test
    public void invalidStringEscape() {
        // \f is not a valid escape sequence
        SmartScriptLexer lexer = new SmartScriptLexer("\"\\f\"");
        lexer.setState(SmartScriptLexerState.TAG);

        // will throw!
        assertThrows(SmartScriptLexerException.class, lexer::nextToken);
    }

    @Test
    public void testUnterminatedString() {
        // A string lacking a closing quote
        SmartScriptLexer lexer = new SmartScriptLexer("\"Opps, forgot to close this string ->");
        lexer.setState(SmartScriptLexerState.TAG);

        // will throw!
        assertThrows(SmartScriptLexerException.class, lexer::nextToken);
    }

    @Test
    public void testUnterminatedEscape() {
        // A string lacking a closing quote, ending in a backslash
        SmartScriptLexer lexer = new SmartScriptLexer("\"Backslash at EOF: \\");
        lexer.setState(SmartScriptLexerState.TAG);

        // will throw!
        assertThrows(SmartScriptLexerException.class, lexer::nextToken);
    }

    @Test
    public void testCombinedInput() {
        // A test of parsing combined input in the tag state...
        SmartScriptLexer lexer = new SmartScriptLexer("  i-5.0test4-4ident5_8@foo2_1-3.0@_=$}-a-6^8bbb\"1\"");
        lexer.setState(SmartScriptLexerState.TAG);

        SmartScriptToken[] correctData = {
                new SmartScriptToken(SmartScriptTokenType.IDENTIFIER, "i"),
                new SmartScriptToken(SmartScriptTokenType.NUMBER, -5.0d),
                new SmartScriptToken(SmartScriptTokenType.IDENTIFIER, "test4"),
                new SmartScriptToken(SmartScriptTokenType.NUMBER, -4d),
                new SmartScriptToken(SmartScriptTokenType.IDENTIFIER, "ident5_8"),
                new SmartScriptToken(SmartScriptTokenType.FUNCTION, "@foo2_1"),
                new SmartScriptToken(SmartScriptTokenType.NUMBER, -3.0d),
                new SmartScriptToken(SmartScriptTokenType.FUNCTION, "@_"),
                new SmartScriptToken(SmartScriptTokenType.EQUALS, null),
                new SmartScriptToken(SmartScriptTokenType.TAG_RIGHT, null),
                new SmartScriptToken(SmartScriptTokenType.OPERATOR, '-'),
                new SmartScriptToken(SmartScriptTokenType.IDENTIFIER, "a"),
                new SmartScriptToken(SmartScriptTokenType.NUMBER, -6.0d),
                new SmartScriptToken(SmartScriptTokenType.OPERATOR, '^'),
                new SmartScriptToken(SmartScriptTokenType.NUMBER, 8.0d),
                new SmartScriptToken(SmartScriptTokenType.IDENTIFIER, "bbb"),
                new SmartScriptToken(SmartScriptTokenType.STRING, "1"),
                new SmartScriptToken(SmartScriptTokenType.EOF, null)
        };

        checkTokenStream(lexer, correctData);
    }

    @Test
    public void testMultipartInput() {
        // Test input which has parts which are tokenized by different rules...
        SmartScriptLexer lexer = new SmartScriptLexer(
                "This is sample text.\r\n" +
                "{$ FOR i 1 10 1 $}\r\n" +
                "  This is {$= i $}-th time this message is generated.\r\n" +
                "{$END$}\r\n" +
                "{$FOR i 0 10 2 $}\r\n" +
                "  sin({$=i$}^2) = {$= i i * @sin \"0.000\" @decfmt $}\r\n" +
                "{$END$}"
        );

        checkToken(lexer.nextToken(), new SmartScriptToken(SmartScriptTokenType.BARE_STRING, "This is sample text.\r\n"));
        checkToken(lexer.nextToken(), new SmartScriptToken(SmartScriptTokenType.TAG_LEFT, null));

        lexer.setState(SmartScriptLexerState.TAG);

        // FOR i 1 10 1
        checkToken(lexer.nextToken(), new SmartScriptToken(SmartScriptTokenType.IDENTIFIER, "FOR"));
        checkToken(lexer.nextToken(), new SmartScriptToken(SmartScriptTokenType.IDENTIFIER, "i"));
        checkToken(lexer.nextToken(), new SmartScriptToken(SmartScriptTokenType.NUMBER, 1d));
        checkToken(lexer.nextToken(), new SmartScriptToken(SmartScriptTokenType.NUMBER, 10d));
        checkToken(lexer.nextToken(), new SmartScriptToken(SmartScriptTokenType.NUMBER, 1d));
        checkToken(lexer.nextToken(), new SmartScriptToken(SmartScriptTokenType.TAG_RIGHT, null));

        lexer.setState(SmartScriptLexerState.TEXT);

        checkToken(lexer.nextToken(), new SmartScriptToken(SmartScriptTokenType.BARE_STRING, "\r\n  This is "));
        checkToken(lexer.nextToken(), new SmartScriptToken(SmartScriptTokenType.TAG_LEFT, null));

        lexer.setState(SmartScriptLexerState.TAG);

        // = i
        checkToken(lexer.nextToken(), new SmartScriptToken(SmartScriptTokenType.EQUALS, null));
        checkToken(lexer.nextToken(), new SmartScriptToken(SmartScriptTokenType.IDENTIFIER, "i"));
        checkToken(lexer.nextToken(), new SmartScriptToken(SmartScriptTokenType.TAG_RIGHT, null));

        lexer.setState(SmartScriptLexerState.TEXT);

        checkToken(lexer.nextToken(), new SmartScriptToken(SmartScriptTokenType.BARE_STRING, "-th time this message is generated.\r\n"));
        checkToken(lexer.nextToken(), new SmartScriptToken(SmartScriptTokenType.TAG_LEFT, null));

        lexer.setState(SmartScriptLexerState.TAG);

        // END
        checkToken(lexer.nextToken(), new SmartScriptToken(SmartScriptTokenType.IDENTIFIER, "END"));
        checkToken(lexer.nextToken(), new SmartScriptToken(SmartScriptTokenType.TAG_RIGHT, null));

        lexer.setState(SmartScriptLexerState.TEXT);

        checkToken(lexer.nextToken(), new SmartScriptToken(SmartScriptTokenType.BARE_STRING, "\r\n"));
        checkToken(lexer.nextToken(), new SmartScriptToken(SmartScriptTokenType.TAG_LEFT, null));

        lexer.setState(SmartScriptLexerState.TAG);

        // FOR i 0 10 2
        checkToken(lexer.nextToken(), new SmartScriptToken(SmartScriptTokenType.IDENTIFIER, "FOR"));
        checkToken(lexer.nextToken(), new SmartScriptToken(SmartScriptTokenType.IDENTIFIER, "i"));
        checkToken(lexer.nextToken(), new SmartScriptToken(SmartScriptTokenType.NUMBER, 0d));
        checkToken(lexer.nextToken(), new SmartScriptToken(SmartScriptTokenType.NUMBER, 10d));
        checkToken(lexer.nextToken(), new SmartScriptToken(SmartScriptTokenType.NUMBER, 2d));
        checkToken(lexer.nextToken(), new SmartScriptToken(SmartScriptTokenType.TAG_RIGHT, null));

        lexer.setState(SmartScriptLexerState.TEXT);

        checkToken(lexer.nextToken(), new SmartScriptToken(SmartScriptTokenType.BARE_STRING, "\r\n  sin("));
        checkToken(lexer.nextToken(), new SmartScriptToken(SmartScriptTokenType.TAG_LEFT, null));

        lexer.setState(SmartScriptLexerState.TAG);

        // = i
        checkToken(lexer.nextToken(), new SmartScriptToken(SmartScriptTokenType.EQUALS, null));
        checkToken(lexer.nextToken(), new SmartScriptToken(SmartScriptTokenType.IDENTIFIER, "i"));
        checkToken(lexer.nextToken(), new SmartScriptToken(SmartScriptTokenType.TAG_RIGHT, null));

        lexer.setState(SmartScriptLexerState.TEXT);

        checkToken(lexer.nextToken(), new SmartScriptToken(SmartScriptTokenType.BARE_STRING, "^2) = "));
        checkToken(lexer.nextToken(), new SmartScriptToken(SmartScriptTokenType.TAG_LEFT, null));

        lexer.setState(SmartScriptLexerState.TAG);

        // i i * @sin "0.000" @decfmt
        checkToken(lexer.nextToken(), new SmartScriptToken(SmartScriptTokenType.EQUALS, null));
        checkToken(lexer.nextToken(), new SmartScriptToken(SmartScriptTokenType.IDENTIFIER, "i"));
        checkToken(lexer.nextToken(), new SmartScriptToken(SmartScriptTokenType.IDENTIFIER, "i"));
        checkToken(lexer.nextToken(), new SmartScriptToken(SmartScriptTokenType.OPERATOR, '*'));
        checkToken(lexer.nextToken(), new SmartScriptToken(SmartScriptTokenType.FUNCTION, "@sin"));
        checkToken(lexer.nextToken(), new SmartScriptToken(SmartScriptTokenType.STRING, "0.000"));
        checkToken(lexer.nextToken(), new SmartScriptToken(SmartScriptTokenType.FUNCTION, "@decfmt"));
        checkToken(lexer.nextToken(), new SmartScriptToken(SmartScriptTokenType.TAG_RIGHT, null));

        lexer.setState(SmartScriptLexerState.TEXT);

        checkToken(lexer.nextToken(), new SmartScriptToken(SmartScriptTokenType.BARE_STRING, "\r\n"));
        checkToken(lexer.nextToken(), new SmartScriptToken(SmartScriptTokenType.TAG_LEFT, null));

        lexer.setState(SmartScriptLexerState.TAG);

        // END
        checkToken(lexer.nextToken(), new SmartScriptToken(SmartScriptTokenType.IDENTIFIER, "END"));
        checkToken(lexer.nextToken(), new SmartScriptToken(SmartScriptTokenType.TAG_RIGHT, null));

        checkToken(lexer.nextToken(), new SmartScriptToken(SmartScriptTokenType.EOF, null));
    }

    private void checkToken(SmartScriptToken actual, SmartScriptToken expected) {
        String msg = "Token are not equal.";
        assertEquals(expected.getType(), actual.getType(), msg);
        assertEquals(expected.getValue(), actual.getValue(), msg);
    }
}
