package hr.fer.oprpp1.hw05.shell;

import hr.fer.oprpp1.hw05.shell.commands.ShellCommand;

import java.util.SortedMap;

/**
 * An interface used in shell commands to interact with the environment.
 *
 * @author Borna Cafuk
 */
public interface Environment {
    /**
     * Reads a line of text from the input. Blocks until input is available.
     *
     * @return the line which was read
     * @throws ShellIOException if a line could not be read, for example if there is no more input
     */
    String readLine() throws ShellIOException;

    /**
     * Writes text to the output.
     *
     * @param text the text to write
     * @throws ShellIOException if the text could not be written, for example if the environment has it redirected to a read-only file
     */
    void write(String text) throws ShellIOException;

    /**
     * Writes text to the output, followed by a newline.
     *
     * @param text the text to write
     * @throws ShellIOException if the text could not be written, for example if the environment has it redirected to a read-only file
     */
    void writeln(String text) throws ShellIOException;

    /**
     * Returns an unmodifiable map of commands supported by the environment.
     *
     * @return a map where the keys are the names of commands, and the values are the command objects
     */
    SortedMap<String, ShellCommand> commands();

    /**
     * Gets the multiline symbol.
     * <p>
     * This is the symbol used by the shell at the beginning of a line to signify that more lines of input are expected.
     *
     * @return the multiline symbol
     */
    Character getMultilineSymbol();

    /**
     * Sets the multiline symbol.
     *
     * @param symbol the new multiline symbol
     * @see #getMultilineSymbol()
     */
    void setMultilineSymbol(Character symbol);

    /**
     * Gets the prompt symbol.
     * <p>
     * This is the symbol used by the shell at the beginning of a line to signify that input is expected.
     *
     * @return the prompt symbol
     */
    Character getPromptSymbol();

    /**
     * Sets the prompt symbol.
     *
     * @param symbol the new prompt symbol
     * @see #getPromptSymbol()
     */
    void setPromptSymbol(Character symbol);

    /**
     * Gets the more lines symbol.
     * <p>
     * This is the symbol used at the end of a line to tell the shell that more lines of input follow.
     *
     * @return the more lines symbol
     */
    Character getMorelinesSymbol();

    /**
     * Sets the more lines symbol.
     *
     * @param symbol the new more lines symbol
     * @see #getMorelinesSymbol()
     */
    void setMorelinesSymbol(Character symbol);
}
