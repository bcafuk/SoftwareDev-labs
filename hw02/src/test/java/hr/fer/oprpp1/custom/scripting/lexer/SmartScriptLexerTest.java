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

        assertEquals(TokenType.EOF, lexer.nextToken().getType(), "Empty input must generate only EOF token.");
    }

    @Test
    public void testGetReturnsLastNext() {
        // Calling getToken once or several times after calling nextToken must return what nextToken returned each time...
        SmartScriptLexer lexer = new SmartScriptLexer("");

        Token token = lexer.nextToken();
        assertEquals(token, lexer.getToken(), "getToken returned different token than nextToken.");
        assertEquals(token, lexer.getToken(), "getToken returned different token than nextToken.");
    }

    @Test
    public void testReadAfterEOF() {
        SmartScriptLexer lexer = new SmartScriptLexer("");

        // will obtain EOF
        lexer.nextToken();
        // will throw!
        assertThrows(LexerException.class, lexer::nextToken);
    }

    @Test
    public void testOnlyWhitespace() {
        String input = "   \r\n\t    ";
        // When the input is only made of spaces, tabs, newlines, etc...
        SmartScriptLexer lexer = new SmartScriptLexer(input);

        // We expect the following stream of tokens
        Token[] correctData = {
                new Token(TokenType.BARE_STRING, input),
                new Token(TokenType.EOF, null)
        };

        checkTokenStream(lexer, correctData);
    }

    @Test
    public void testBasicText() {
        String input = "  Štefanija\r\n\t Automobil   ";
        // Let's check for several words...
        SmartScriptLexer lexer = new SmartScriptLexer(input);

        // We expect the following stream of tokens
        Token[] correctData = {
                new Token(TokenType.BARE_STRING, input),
                new Token(TokenType.EOF, null)
        };

        checkTokenStream(lexer, correctData);
    }

    @Test
    public void testTagOpening() {
        // Let's check for a tag opening in the middle of words
        SmartScriptLexer lexer = new SmartScriptLexer("  text  {$ more text ");

        // We expect the following stream of tokens
        Token[] correctData = {
                new Token(TokenType.BARE_STRING, "  text  "),
                new Token(TokenType.TAG_LEFT, null),
                new Token(TokenType.BARE_STRING, " more text "),
                new Token(TokenType.EOF, null)
        };

        checkTokenStream(lexer, correctData);
    }

    @Test
    public void testLeftBrace() {
        // A left brace which is not part of a tag opening
        String input = "Hello { $ world!";
        SmartScriptLexer lexer = new SmartScriptLexer(input);

        // We expect the following stream of tokens
        Token[] correctData = {
                new Token(TokenType.BARE_STRING, input),
                new Token(TokenType.EOF, null)
        };

        checkTokenStream(lexer, correctData);
    }

    @Test
    public void testWordStartingWithEscape() {
        SmartScriptLexer lexer = new SmartScriptLexer("\\{$FOR$}");

        // We expect the following stream of tokens
        Token[] correctData = {
                new Token(TokenType.BARE_STRING, "{$FOR$}"),
                new Token(TokenType.EOF, null)
        };

        checkTokenStream(lexer, correctData);
    }

    @Test
    public void testInvalidEscapeEnding() {
        SmartScriptLexer lexer = new SmartScriptLexer("   \\");  // this is three spaces and a single backslash -- 4 letters string

        // will throw!
        assertThrows(LexerException.class, lexer::nextToken);
    }

    @Test
    public void testInvalidEscape() {
        SmartScriptLexer lexer = new SmartScriptLexer("   \\a    ");

        // will throw!
        assertThrows(LexerException.class, lexer::nextToken);
    }

    @Test
    public void testWordWithManyEscapes() {
        // Let's check for several escapes...
        SmartScriptLexer lexer = new SmartScriptLexer("Example { bla } blu \\{$=1$}. Nothing interesting {=here}.");

        // We expect the input to be just one bare string
        Token[] correctData = {
                new Token(TokenType.BARE_STRING, "Example { bla } blu {$=1$}. Nothing interesting {=here}."),
                new Token(TokenType.EOF, null)
        };

        checkTokenStream(lexer, correctData);
    }

    // Helper method for checking if lexer generates the same stream of tokens
    // as the given stream.
    private void checkTokenStream(SmartScriptLexer lexer, Token[] correctData) {
        int counter = 0;
        for (Token expected : correctData) {
            Token actual = lexer.nextToken();
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
        lexer.setState(LexerState.TAG);

        assertNotNull(lexer.nextToken(), "Token was expected but null was returned.");
    }

    @Test
    public void testEmptyInTag() {
        SmartScriptLexer lexer = new SmartScriptLexer("");
        lexer.setState(LexerState.TAG);

        assertEquals(TokenType.EOF, lexer.nextToken().getType(), "Empty input must generate only EOF token.");
    }

    @Test
    public void testGetReturnsLastNextInTag() {
        // Calling getToken once or several times after calling nextToken must return what nextToken returned each time...
        SmartScriptLexer lexer = new SmartScriptLexer("");
        lexer.setState(LexerState.TAG);

        Token token = lexer.nextToken();
        assertEquals(token, lexer.getToken(), "getToken returned different token than nextToken.");
        assertEquals(token, lexer.getToken(), "getToken returned different token than nextToken.");
    }

    @Test
    public void testReadAfterEOFInTag() {
        SmartScriptLexer lexer = new SmartScriptLexer("");
        lexer.setState(LexerState.TAG);

        // will obtain EOF
        lexer.nextToken();
        // will throw!
        assertThrows(LexerException.class, lexer::nextToken);
    }

    @Test
    public void testNoActualContentInTag() {
        // When input is only of spaces, tabs, newlines, etc...
        SmartScriptLexer lexer = new SmartScriptLexer("   \r\n\t    ");
        lexer.setState(LexerState.TAG);

        assertEquals(TokenType.EOF, lexer.nextToken().getType(), "Input had no content. SmartScriptLexer should generated only EOF token.");
    }

    @Test
    public void testOnlyWhitespaceInTag() {
        // Whitespace should get ignored
        SmartScriptLexer lexer = new SmartScriptLexer("  \r\n\t   ");
        lexer.setState(LexerState.TAG);

        assertEquals(TokenType.EOF, lexer.nextToken().getType(), "Input had only whitespace. SmartScriptLexer should generated only EOF token.");
    }

    @Test
    public void testTwoNumbers() {
        // Let's check for several numbers...
        SmartScriptLexer lexer = new SmartScriptLexer("  -1234\r\n\t 5678.5   ");
        lexer.setState(LexerState.TAG);

        Token[] correctData = {
                new Token(TokenType.NUMBER, -1234d),
                new Token(TokenType.NUMBER, 5678.5d),
                new Token(TokenType.EOF, null)
        };

        checkTokenStream(lexer, correctData);
    }

    @Test
    public void testDoublePeriodsNumber() {
        SmartScriptLexer lexer = new SmartScriptLexer("  -12.75.56   ");
        lexer.setState(LexerState.TAG);

        // Expect the number before the second period to be lexed correctly
        checkToken(lexer.nextToken(), new Token(TokenType.NUMBER, -12.75d));

        // will throw!
        assertThrows(LexerException.class, lexer::nextToken);
    }

    @Test
    public void testIdentifiers() {
        // Let's check for several identifiers...
        SmartScriptLexer lexer = new SmartScriptLexer("  i FOR\n pi_2 _   ");
        lexer.setState(LexerState.TAG);

        Token[] correctData = {
                new Token(TokenType.IDENTIFIER, "i"),
                new Token(TokenType.IDENTIFIER, "FOR"),
                new Token(TokenType.IDENTIFIER, "pi_2"),
                new Token(TokenType.IDENTIFIER, "_"),
                new Token(TokenType.EOF, null)
        };

        checkTokenStream(lexer, correctData);
    }

    @Test
    public void testFunctions() {
        // Let's check for several functions...
        SmartScriptLexer lexer = new SmartScriptLexer("  @i @FOR\n @pi_2@_   ");
        lexer.setState(LexerState.TAG);

        Token[] correctData = {
                new Token(TokenType.FUNCTION, "@i"),
                new Token(TokenType.FUNCTION, "@FOR"),
                new Token(TokenType.FUNCTION, "@pi_2"),
                new Token(TokenType.FUNCTION, "@_"),
                new Token(TokenType.EOF, null)
        };

        checkTokenStream(lexer, correctData);
    }

    @Test
    public void testTagClosing() {
        // Let's check for a closing tag
        SmartScriptLexer lexer = new SmartScriptLexer("ident$}5");
        lexer.setState(LexerState.TAG);

        // We expect the following stream of tokens
        Token[] correctData = {
                new Token(TokenType.IDENTIFIER, "ident"),
                new Token(TokenType.TAG_RIGHT, null),
                new Token(TokenType.NUMBER, 5d),
                new Token(TokenType.EOF, null)
        };

        checkTokenStream(lexer, correctData);
    }

    @Test
    public void testEquals() {
        // Let's check for an equals symbols
        SmartScriptLexer lexer = new SmartScriptLexer("  \n =  \n i i == =");
        lexer.setState(LexerState.TAG);

        // We expect the following stream of tokens
        Token[] correctData = {
                new Token(TokenType.EQUALS, null),
                new Token(TokenType.IDENTIFIER, "i"),
                new Token(TokenType.IDENTIFIER, "i"),
                new Token(TokenType.EQUALS, null),
                new Token(TokenType.EQUALS, null),
                new Token(TokenType.EQUALS, null),
                new Token(TokenType.EOF, null)
        };

        checkTokenStream(lexer, correctData);
    }

    @Test
    public void testOperators() {
        // Test operators
        SmartScriptLexer lexer = new SmartScriptLexer("  +-*/^ a+5/i-@foo\n-\n15");
        lexer.setState(LexerState.TAG);

        // We expect the following stream of tokens
        Token[] correctData = {
                new Token(TokenType.OPERATOR, '+'),
                new Token(TokenType.OPERATOR, '-'),
                new Token(TokenType.OPERATOR, '*'),
                new Token(TokenType.OPERATOR, '/'),
                new Token(TokenType.OPERATOR, '^'),
                new Token(TokenType.IDENTIFIER, "a"),
                new Token(TokenType.OPERATOR, '+'),
                new Token(TokenType.NUMBER, 5d),
                new Token(TokenType.OPERATOR, '/'),
                new Token(TokenType.IDENTIFIER, "i"),
                new Token(TokenType.OPERATOR, '-'),
                new Token(TokenType.FUNCTION, "@foo"),
                new Token(TokenType.OPERATOR, '-'),
                new Token(TokenType.NUMBER, 15d),
                new Token(TokenType.EOF, null)
        };

        checkTokenStream(lexer, correctData);
    }

    @Test
    public void testMinus() {
        // The minus character can both start a number and be an operator, this is tested here
        SmartScriptLexer lexer = new SmartScriptLexer("- 5-4-a-");
        lexer.setState(LexerState.TAG);

        // We expect the following stream of tokens
        Token[] correctData = {
                new Token(TokenType.OPERATOR, '-'),
                new Token(TokenType.NUMBER, 5d),
                new Token(TokenType.NUMBER, -4d),
                new Token(TokenType.OPERATOR, '-'),
                new Token(TokenType.IDENTIFIER, "a"),
                new Token(TokenType.OPERATOR, '-'),
                new Token(TokenType.EOF, null)
        };

        checkTokenStream(lexer, correctData);
    }

    @Test
    public void testStrings() {
        // Two simple strings
        SmartScriptLexer lexer = new SmartScriptLexer("    \"Hello, world!\"\"$}=@foo\"    ");
        lexer.setState(LexerState.TAG);

        // We expect the following stream of tokens
        Token[] correctData = {
                new Token(TokenType.STRING, "Hello, world!"),
                new Token(TokenType.STRING, "$}=@foo"),
                new Token(TokenType.EOF, null)
        };

        checkTokenStream(lexer, correctData);
    }

    @Test
    public void testStringEscapes() {
        // Two simple strings
        SmartScriptLexer lexer = new SmartScriptLexer("\"Joe \\\"Long\\\" Smith\" \"\\\\\\\"\" \"H\\ne\\tl\\rl\\\\o\\\"!\"");
        lexer.setState(LexerState.TAG);

        // We expect the following stream of tokens
        Token[] correctData = {
                new Token(TokenType.STRING, "Joe \"Long\" Smith"),
                new Token(TokenType.STRING, "\\\""),
                new Token(TokenType.STRING, "H\ne\tl\rl\\o\"!"),
                new Token(TokenType.EOF, null)
        };

        checkTokenStream(lexer, correctData);
    }

    @Test
    public void invalidStringEscape() {
        // \f is not a valid escape sequence
        SmartScriptLexer lexer = new SmartScriptLexer("\"\\f\"");
        lexer.setState(LexerState.TAG);

        // will throw!
        assertThrows(LexerException.class, lexer::nextToken);
    }

    @Test
    public void testUnterminatedString() {
        // A string lacking a closing quote
        SmartScriptLexer lexer = new SmartScriptLexer("\"Opps, forgot to close this string ->");
        lexer.setState(LexerState.TAG);

        // will throw!
        assertThrows(LexerException.class, lexer::nextToken);
    }

    @Test
    public void testUnterminatedEscape() {
        // A string lacking a closing quote, ending in a backslash
        SmartScriptLexer lexer = new SmartScriptLexer("\"Backslash at EOF: \\");
        lexer.setState(LexerState.TAG);

        // will throw!
        assertThrows(LexerException.class, lexer::nextToken);
    }

    @Test
    public void testCombinedInput() {
        // A test of parsing combined input in the tag state...
        SmartScriptLexer lexer = new SmartScriptLexer("  i-5.0test4-4ident5_8@foo2_1-3.0@_=$}-a-6^8bbb\"1\"");
        lexer.setState(LexerState.TAG);

        Token[] correctData = {
                new Token(TokenType.IDENTIFIER, "i"),
                new Token(TokenType.NUMBER, -5.0d),
                new Token(TokenType.IDENTIFIER, "test4"),
                new Token(TokenType.NUMBER, -4d),
                new Token(TokenType.IDENTIFIER, "ident5_8"),
                new Token(TokenType.FUNCTION, "@foo2_1"),
                new Token(TokenType.NUMBER, -3.0d),
                new Token(TokenType.FUNCTION, "@_"),
                new Token(TokenType.EQUALS, null),
                new Token(TokenType.TAG_RIGHT, null),
                new Token(TokenType.OPERATOR, '-'),
                new Token(TokenType.IDENTIFIER, "a"),
                new Token(TokenType.NUMBER, -6.0d),
                new Token(TokenType.OPERATOR, '^'),
                new Token(TokenType.NUMBER, 8.0d),
                new Token(TokenType.IDENTIFIER, "bbb"),
                new Token(TokenType.STRING, "1"),
                new Token(TokenType.EOF, null)
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

        checkToken(lexer.nextToken(), new Token(TokenType.BARE_STRING, "This is sample text.\r\n"));
        checkToken(lexer.nextToken(), new Token(TokenType.TAG_LEFT, null));

        lexer.setState(LexerState.TAG);

        // FOR i 1 10 1
        checkToken(lexer.nextToken(), new Token(TokenType.IDENTIFIER, "FOR"));
        checkToken(lexer.nextToken(), new Token(TokenType.IDENTIFIER, "i"));
        checkToken(lexer.nextToken(), new Token(TokenType.NUMBER, 1d));
        checkToken(lexer.nextToken(), new Token(TokenType.NUMBER, 10d));
        checkToken(lexer.nextToken(), new Token(TokenType.NUMBER, 1d));
        checkToken(lexer.nextToken(), new Token(TokenType.TAG_RIGHT, null));

        lexer.setState(LexerState.TEXT);

        checkToken(lexer.nextToken(), new Token(TokenType.BARE_STRING, "\r\n  This is "));
        checkToken(lexer.nextToken(), new Token(TokenType.TAG_LEFT, null));

        lexer.setState(LexerState.TAG);

        // = i
        checkToken(lexer.nextToken(), new Token(TokenType.EQUALS, null));
        checkToken(lexer.nextToken(), new Token(TokenType.IDENTIFIER, "i"));
        checkToken(lexer.nextToken(), new Token(TokenType.TAG_RIGHT, null));

        lexer.setState(LexerState.TEXT);

        checkToken(lexer.nextToken(), new Token(TokenType.BARE_STRING, "-th time this message is generated.\r\n"));
        checkToken(lexer.nextToken(), new Token(TokenType.TAG_LEFT, null));

        lexer.setState(LexerState.TAG);

        // END
        checkToken(lexer.nextToken(), new Token(TokenType.IDENTIFIER, "END"));
        checkToken(lexer.nextToken(), new Token(TokenType.TAG_RIGHT, null));

        lexer.setState(LexerState.TEXT);

        checkToken(lexer.nextToken(), new Token(TokenType.BARE_STRING, "\r\n"));
        checkToken(lexer.nextToken(), new Token(TokenType.TAG_LEFT, null));

        lexer.setState(LexerState.TAG);

        // FOR i 0 10 2
        checkToken(lexer.nextToken(), new Token(TokenType.IDENTIFIER, "FOR"));
        checkToken(lexer.nextToken(), new Token(TokenType.IDENTIFIER, "i"));
        checkToken(lexer.nextToken(), new Token(TokenType.NUMBER, 0d));
        checkToken(lexer.nextToken(), new Token(TokenType.NUMBER, 10d));
        checkToken(lexer.nextToken(), new Token(TokenType.NUMBER, 2d));
        checkToken(lexer.nextToken(), new Token(TokenType.TAG_RIGHT, null));

        lexer.setState(LexerState.TEXT);

        checkToken(lexer.nextToken(), new Token(TokenType.BARE_STRING, "\r\n  sin("));
        checkToken(lexer.nextToken(), new Token(TokenType.TAG_LEFT, null));

        lexer.setState(LexerState.TAG);

        // = i
        checkToken(lexer.nextToken(), new Token(TokenType.EQUALS, null));
        checkToken(lexer.nextToken(), new Token(TokenType.IDENTIFIER, "i"));
        checkToken(lexer.nextToken(), new Token(TokenType.TAG_RIGHT, null));

        lexer.setState(LexerState.TEXT);

        checkToken(lexer.nextToken(), new Token(TokenType.BARE_STRING, "^2) = "));
        checkToken(lexer.nextToken(), new Token(TokenType.TAG_LEFT, null));

        lexer.setState(LexerState.TAG);

        // i i * @sin "0.000" @decfmt
        checkToken(lexer.nextToken(), new Token(TokenType.EQUALS, null));
        checkToken(lexer.nextToken(), new Token(TokenType.IDENTIFIER, "i"));
        checkToken(lexer.nextToken(), new Token(TokenType.IDENTIFIER, "i"));
        checkToken(lexer.nextToken(), new Token(TokenType.OPERATOR, '*'));
        checkToken(lexer.nextToken(), new Token(TokenType.FUNCTION, "@sin"));
        checkToken(lexer.nextToken(), new Token(TokenType.STRING, "0.000"));
        checkToken(lexer.nextToken(), new Token(TokenType.FUNCTION, "@decfmt"));
        checkToken(lexer.nextToken(), new Token(TokenType.TAG_RIGHT, null));

        lexer.setState(LexerState.TEXT);

        checkToken(lexer.nextToken(), new Token(TokenType.BARE_STRING, "\r\n"));
        checkToken(lexer.nextToken(), new Token(TokenType.TAG_LEFT, null));

        lexer.setState(LexerState.TAG);

        // END
        checkToken(lexer.nextToken(), new Token(TokenType.IDENTIFIER, "END"));
        checkToken(lexer.nextToken(), new Token(TokenType.TAG_RIGHT, null));

        checkToken(lexer.nextToken(), new Token(TokenType.EOF, null));
    }

    private void checkToken(Token actual, Token expected) {
        String msg = "Token are not equal.";
        assertEquals(expected.getType(), actual.getType(), msg);
        assertEquals(expected.getValue(), actual.getValue(), msg);
    }
}
