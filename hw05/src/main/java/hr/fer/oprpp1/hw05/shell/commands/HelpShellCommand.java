package hr.fer.oprpp1.hw05.shell.commands;

import hr.fer.oprpp1.hw05.shell.ArgumentParser;
import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellStatus;

import java.util.List;
import java.util.Objects;

/**
 * A command to print the available commands or the usage of a command.
 *
 * @author Borna Cafuk
 */
public class HelpShellCommand implements ShellCommand {
    private static final List<String> DESCRIPTION = List.of(
            "Prints all available commands or the usage information for a command",
            "usage: help [command]"
    );

    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        Objects.requireNonNull(env, "The environment must not be null.");
        Objects.requireNonNull(arguments, "The argument string must not be null.");

        List<String> parsedArguments;
        try {
            parsedArguments = ArgumentParser.parseAll(arguments);
        } catch (IllegalArgumentException e) {
            env.writeln("Invalid argument format");
            return ShellStatus.CONTINUE;
        }

        if (parsedArguments.size() == 0)
            listCommands(env);
        else
            printDescription(env, parsedArguments.get(0));

        return ShellStatus.CONTINUE;
    }

    /**
     * Writes a list of available commands to the environment.
     *
     * @param env the environment to write to
     * @throws NullPointerException if {@code env} is {@code null}
     */
    private void listCommands(Environment env) {
        env.writeln("Available commands:");
        for (String command : env.commands().keySet()) {
            env.write(" ");
            env.writeln(command);
        }
        env.writeln("To see more information about a command, write help command_name");
    }

    /**
     * Writes the description of a command to the environment.
     *
     * @param env         the environment to write to
     * @param commandName the name of the command whose description to write
     * @throws NullPointerException if {@code env} or {@code commandName} is {@code null}
     */
    private void printDescription(Environment env, String commandName) {
        ShellCommand command = env.commands().get(Objects.requireNonNull(commandName));

        if (command == null)
            env.writeln("Unknown command " + commandName + ", to see the list of available commands, write help");
        else
            for (String line : command.getCommandDescription())
                env.writeln(line);
    }

    @Override
    public String getCommandName() {
        return "help";
    }

    @Override
    public List<String> getCommandDescription() {
        return DESCRIPTION;
    }
}
