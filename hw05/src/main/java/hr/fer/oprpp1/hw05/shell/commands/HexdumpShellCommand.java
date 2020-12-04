package hr.fer.oprpp1.hw05.shell.commands;

import hr.fer.oprpp1.hw05.shell.ArgumentParser;
import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellStatus;
import hr.fer.oprpp1.hw05.shell.Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
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
public class HexdumpShellCommand implements ShellCommand {
    private static final List<String> DESCRIPTION = List.of(
            "Prints a file's contents to the console in hexadecimal",
            "usage: hexdump path"
    );

    private static final int BYTES_PER_LINE = 16;
    private static final int BYTES_PER_DIVISION = 8;

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

        int offset = 0;
        try (InputStream stream = Files.newInputStream(path)) {
            while (printHexLine(env, stream, offset))
                offset += BYTES_PER_LINE;

        } catch (NoSuchFileException e) {
            env.writeln("No such file: " + parsedArguments.get(0));
        } catch (IOException | UncheckedIOException e) {
            env.writeln("I/O exception: " + e.toString());
        }
        // TODO: Handle AccessDeniedException.

        return ShellStatus.CONTINUE;
    }

    @Override
    public String getCommandName() {
        return "hexdump";
    }

    @Override
    public List<String> getCommandDescription() {
        return DESCRIPTION;
    }

    private boolean printHexLine(Environment env, InputStream stream, int offset) throws IOException {
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("%08X:", offset));

        byte[] buffer = new byte[BYTES_PER_LINE];

        int bytesRead = stream.read(buffer);
        if (bytesRead == -1)
            bytesRead = 0;

        for (int i = 0; i < BYTES_PER_LINE; i++) {
            if (i != 0 && i % BYTES_PER_DIVISION == 0)
                sb.append("|");
            else
                sb.append(" ");

            if (i < bytesRead)
                sb.append(String.format("%02X", buffer[i] & 0xFF));
            else
                sb.append("  ");
        }

        sb.append(" | ");

        for (int i = 0; i < bytesRead; i++) {
            char c = (char) (buffer[i] & 0xFF);
            boolean isPrintable = c >= ' ' && c <= '~';

            sb.append(isPrintable ? c : '.');
        }

        env.writeln(sb.toString());

        return bytesRead == BYTES_PER_LINE;
    }
}
