package hr.fer.oprpp1.hw05.shell.commands;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellStatus;

import java.util.List;
import java.util.Objects;

/**
 * A command which exits the shell.
 *
 * @author Borna Cafuk
 */
public class ExitShellCommand implements ShellCommand {
    private static final List<String> DESCRIPTION = List.of(
            "Terminates the shell",
            "usage: exit"
    );

    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        Objects.requireNonNull(env, "The environment must not be null.");
        Objects.requireNonNull(arguments, "The argument string must not be null.");

        return ShellStatus.TERMINATE;
    }

    @Override
    public String getCommandName() {
        return "exit";
    }

    @Override
    public List<String> getCommandDescription() {
        return DESCRIPTION;
    }
}
