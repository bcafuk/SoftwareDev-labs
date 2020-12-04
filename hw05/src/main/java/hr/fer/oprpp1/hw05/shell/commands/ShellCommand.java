package hr.fer.oprpp1.hw05.shell.commands;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellStatus;

import java.util.List;

/**
 * A command to be executed by the shell.
 */
public interface ShellCommand {
    /**
     * Executes a command.
     *
     * @param env       the environment in which to run the command
     * @param arguments a string containing the command's arguments
     * @return the expected status of the shell after the command is run; that is,
     *         whether the shell should terminate after the command
     * @throws NullPointerException if {@code env} or {@code arguments} is {@code null}
     */
    ShellStatus executeCommand(Environment env, String arguments);

    /**
     * Retrieves the name of the command.
     *
     * @return the name of the command
     */
    String getCommandName();

    /**
     * Retrieves the description and usage instructions of the command.
     *
     * @return a read-only list containing the lines of the description
     */
    List<String> getCommandDescription();
}
