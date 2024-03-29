package hr.fer.oprpp1.hw05.shell.commands;

import hr.fer.oprpp1.hw05.shell.ArgumentParser;
import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellStatus;
import hr.fer.oprpp1.hw05.shell.Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

/**
 * Writes a file's contents to the environment.
 *
 * @author Borna Cafuk
 */
public class CatShellCommand implements ShellCommand {
    private static final List<String> DESCRIPTION = List.of(
            "Prints a file's contents to the console",
            "usage: cat path [charset]"
    );

    /**
     * The size of the character buffer used to read in data from the file.
     */
    private static final int BUFFER_SIZE = 1024;

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
            env.writeln("Please enter a file name");
            return ShellStatus.CONTINUE;
        }


        Path path = Util.getPath(parsedArguments.get(0), env);
        if (path == null)
            return ShellStatus.CONTINUE;

        Charset cs;

        if (parsedArguments.size() < 2) {
            cs = Charset.defaultCharset();
        } else {
            try {
                cs = Charset.forName(parsedArguments.get(1));
            } catch (IllegalCharsetNameException e) {
                env.writeln("Illegal charset name: " + parsedArguments.get(1));
                return ShellStatus.CONTINUE;
            } catch (UnsupportedCharsetException e) {
                env.writeln("Unsupported charset: " + parsedArguments.get(1));
                return ShellStatus.CONTINUE;
            }
        }

        try (BufferedReader reader = Files.newBufferedReader(path, cs)) {
            char[] buffer = new char[BUFFER_SIZE];

            while (true) {
                int charsRead = reader.read(buffer);

                if (charsRead == -1)
                    break;

                env.write(String.valueOf(buffer, 0, charsRead));
            }
            env.writeln("");
        } catch (AccessDeniedException e) {
            env.writeln("Access denied");
        } catch (NoSuchFileException e) {
            env.writeln("No such file: " + parsedArguments.get(0));
        } catch (IOException | UncheckedIOException e) {
            env.writeln("I/O exception: " + e.toString());
        }

        return ShellStatus.CONTINUE;
    }

    @Override
    public String getCommandName() {
        return "cat";
    }

    @Override
    public List<String> getCommandDescription() {
        return DESCRIPTION;
    }
}
