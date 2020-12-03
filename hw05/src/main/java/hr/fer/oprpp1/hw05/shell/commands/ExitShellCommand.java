package hr.fer.oprpp1.hw05.shell.commands;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellStatus;

import java.util.Collections;
import java.util.List;

/**
 * A command which exits the shell.
 *
 * @author Borna Cafuk
 */
public class ExitShellCommand implements ShellCommand {
    private static final List<String> DESCRIPTION = Collections.unmodifiableList(List.of(
            "Terminates the shell",
            "usage: exit"
    ));

    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
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
