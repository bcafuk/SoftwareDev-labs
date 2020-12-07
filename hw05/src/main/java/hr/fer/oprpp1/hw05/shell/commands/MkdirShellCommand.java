package hr.fer.oprpp1.hw05.shell.commands;

import hr.fer.oprpp1.hw05.shell.ArgumentParser;
import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellStatus;
import hr.fer.oprpp1.hw05.shell.Util;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

/**
 * Creates a directory.
 *
 * @author Borna Cafuk
 */
public class MkdirShellCommand implements ShellCommand {
    private static final List<String> DESCRIPTION = List.of(
            "Creates a directory",
            "usage: mkdir path"
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

        if (parsedArguments.size() == 0) {
            env.writeln("Please enter a directory path");
            return ShellStatus.CONTINUE;
        }

        Path path = Util.getPath(parsedArguments.get(0), env);
        if (path == null)
            return ShellStatus.CONTINUE;

        try {
            Files.createDirectories(path);
        } catch (FileAlreadyExistsException e) {
            env.writeln("A file already exists with that path");
        } catch (IOException e) {
            env.writeln("I/O exception: " + e.toString());
        }

        return ShellStatus.CONTINUE;
    }

    @Override
    public String getCommandName() {
        return "mkdir";
    }

    @Override
    public List<String> getCommandDescription() {
        return DESCRIPTION;
    }
}
