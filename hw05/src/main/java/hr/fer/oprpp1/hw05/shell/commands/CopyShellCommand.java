package hr.fer.oprpp1.hw05.shell.commands;

import hr.fer.oprpp1.hw05.shell.ArgumentParser;
import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellStatus;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

/**
 * Copies a file.
 *
 * @author Borna Cafuk
 */
public class CopyShellCommand implements ShellCommand {
    private static final List<String> DESCRIPTION = List.of(
            "Copies a file",
            "usage: copy source_file destination_file",
            "       copy source_file destination_directory"
    );

    private static final String AFFIRMATIVE_RESPONSE = "y";
    private static final String NEGATIVE_RESPONSE = "n";

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

        if (parsedArguments.size() < 2) {
            env.writeln("Please enter source and destination paths");
            return ShellStatus.CONTINUE;
        }

        Path source;
        try {
            source = Paths.get(parsedArguments.get(0));
        } catch (InvalidPathException e) {
            env.writeln("Invalid path: " + parsedArguments.get(0));
            return ShellStatus.CONTINUE;
        }

        if (!Files.exists(source)) {
            env.writeln(parsedArguments.get(0) + " does not exist");
            return ShellStatus.CONTINUE;
        }

        if (Files.isDirectory(source)) {
            env.writeln(parsedArguments.get(0) + " is a directory");
            return ShellStatus.CONTINUE;
        }

        Path destination;
        try {
            destination = Paths.get(parsedArguments.get(1));
        } catch (InvalidPathException e) {
            env.writeln("Invalid path: " + parsedArguments.get(1));
            return ShellStatus.CONTINUE;
        }

        if (Files.isDirectory(destination)) {
            Path newDestination = destination.resolve(source.toAbsolutePath().normalize().getFileName());

            if (Files.isDirectory(newDestination)) {
                env.writeln(newDestination.getFileName() + " already exists in " + destination.getFileName() + " and is a a directory");
                return ShellStatus.CONTINUE;
            }

            destination = newDestination;
        }

        if (Files.exists(destination)) {
            String response;
            do {
                env.write(destination.getFileName() + " exists. Overwrite it? (" +
                        AFFIRMATIVE_RESPONSE + "/" + NEGATIVE_RESPONSE + ") ");
                response = env.readLine();
            } while (!response.equals(AFFIRMATIVE_RESPONSE) && !response.equals(NEGATIVE_RESPONSE));

            if (response.equals(NEGATIVE_RESPONSE)) {
                return ShellStatus.CONTINUE;
            }
        }

        try (InputStream input = Files.newInputStream(source);
             OutputStream output = Files.newOutputStream(destination)) {
            input.transferTo(output);
            env.writeln("File copied");
        } catch (IOException e) {
            env.writeln("I/O exception: " + e.toString());
            return ShellStatus.CONTINUE;
        }

        return ShellStatus.CONTINUE;
    }

    @Override
    public String getCommandName() {
        return "copy";
    }

    @Override
    public List<String> getCommandDescription() {
        return DESCRIPTION;
    }
}
