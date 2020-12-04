package hr.fer.oprpp1.hw05.shell;

import hr.fer.oprpp1.hw05.shell.commands.ShellCommand;

import java.io.*;
import java.util.Collections;
import java.util.SortedMap;

/**
 * An implementation of the {@link Environment} interface using {@link System#in} and {@link System#out} for I/O.
 *
 * @author Borna Cafuk
 */
public class StreamEnvironment implements Environment {
    /**
     * The reader used for input.
     */
    private final BufferedReader input;
    /**
     * The stream used for output.
     */
    private final PrintStream output;
    /**
     * An unmodifiable map used to store the commands available in the environment.
     */
    private final SortedMap<String, ShellCommand> commands;

    /**
     * The symbol used to indicate multiline prompts.
     *
     * @see Environment#getMultilineSymbol()
     */
    private char multilineSymbol = '|';
    /**
     * The symbol used to indicate the first line of prompts.
     *
     * @see Environment#getPromptSymbol()
     */
    private char promptSymbol = '>';
    /**
     * The symbol used to indicate that more lines of input follow.
     *
     * @see Environment#getMorelinesSymbol()
     */
    private char morelinesSymbol = '\\';

    /**
     * Constructs an environment which reads and writes using streams.
     *
     * @param input    the input stream
     * @param output   the output stream
     * @param commands a map of commands supported by the environment
     */
    public StreamEnvironment(InputStream input, PrintStream output, SortedMap<String, ShellCommand> commands) {
        this.input = new BufferedReader(new InputStreamReader(input));
        this.output = output;
        this.commands = Collections.unmodifiableSortedMap(commands);
    }

    @Override
    public String readLine() throws ShellIOException {
        try {
            return input.readLine();
        } catch (IOException e) {
            throw new ShellIOException("The input does not contain any more lines.", e);
        }
    }

    @Override
    public void write(String text) throws ShellIOException {
        output.print(text);
        output.flush();
    }

    @Override
    public void writeln(String text) throws ShellIOException {
        output.println(text);
        output.flush();
    }

    @Override
    public SortedMap<String, ShellCommand> commands() {
        return commands;
    }

    @Override
    public char getMultilineSymbol() {
        return multilineSymbol;
    }

    @Override
    public void setMultilineSymbol(char symbol) {
        multilineSymbol = symbol;
    }

    @Override
    public char getPromptSymbol() {
        return promptSymbol;
    }

    @Override
    public void setPromptSymbol(char symbol) {
        promptSymbol = symbol;
    }

    @Override
    public char getMorelinesSymbol() {
        return morelinesSymbol;
    }

    @Override
    public void setMorelinesSymbol(char symbol) {
        morelinesSymbol = symbol;
    }
}
