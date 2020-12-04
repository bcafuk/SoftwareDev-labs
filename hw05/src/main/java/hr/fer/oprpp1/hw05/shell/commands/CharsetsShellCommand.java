package hr.fer.oprpp1.hw05.shell.commands;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellStatus;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Objects;

/**
 * A command which prints supported character sets.
 *
 * @author Borna Cafuk
 */
public class CharsetsShellCommand implements ShellCommand {
    private static final List<String> DESCRIPTION = List.of(
            "Prints the character sets supported on the machine",
            "usage: charsets"
    );

    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        Objects.requireNonNull(env, "The environment must not be null.");
        Objects.requireNonNull(arguments, "The argument string must not be null.");

        for (String charset : Charset.availableCharsets().keySet())
            env.writeln(charset);

        return ShellStatus.CONTINUE;
    }

    @Override
    public String getCommandName() {
        return "charsets";
    }

    @Override
    public List<String> getCommandDescription() {
        return DESCRIPTION;
    }
}
