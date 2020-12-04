package hr.fer.oprpp1.hw05.shell.commands;

import hr.fer.oprpp1.hw05.shell.ArgumentParser;
import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellStatus;
import hr.fer.oprpp1.hw05.shell.Util;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Prints a directory listing.
 *
 * @author Borna Cafuk
 */
public class LsShellCommand implements ShellCommand {
    private static final List<String> DESCRIPTION = List.of(
            "Lists the contents of a directory",
            "usage: ls path"
    );

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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
            Files.list(path)
                 .forEach(item -> {
                     BasicFileAttributeView faView = Files.getFileAttributeView(item,
                             BasicFileAttributeView.class, LinkOption.NOFOLLOW_LINKS);

                     BasicFileAttributes attributes;
                     try {
                         attributes = faView.readAttributes();
                     } catch (IOException e) {
                         throw new UncheckedIOException(e);
                     }

                     FileTime fileTime = attributes.creationTime();
                     String formattedDateTime = DATE_FORMAT.format(new Date(fileTime.toMillis()));

                     env.writeln(String.format("%c%c%c%c %10d %s %s",
                             Files.isDirectory(item) ? 'd' : '-',
                             Files.isReadable(item) ? 'r' : '-',
                             Files.isWritable(item) ? 'w' : '-',
                             Files.isExecutable(item) ? 'x' : '-',
                             attributes.size(),
                             formattedDateTime,
                             item.getFileName()
                     ));
                 });
        } catch (NoSuchFileException e) {
            env.writeln("No such directory: " + parsedArguments.get(0));
        } catch (NotDirectoryException e) {
            env.writeln(parsedArguments.get(0) + " is not a directory");
        } catch (IOException | UncheckedIOException e) {
            env.writeln("I/O exception: " + e.toString());
        }

        return ShellStatus.CONTINUE;
    }

    @Override
    public String getCommandName() {
        return "ls";
    }

    @Override
    public List<String> getCommandDescription() {
        return DESCRIPTION;
    }
}
