package hr.fer.oprpp1.hw05.shell.commands;

import hr.fer.oprpp1.hw05.shell.ArgumentParser;
import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellStatus;
import hr.fer.oprpp1.hw05.shell.Util;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.Objects;

/**
 * Prints a directory tree structure.
 *
 * @author Borna Cafuk
 */
public class TreeShellCommand implements ShellCommand {
    private static final List<String> DESCRIPTION = List.of(
            "Prints a directory tree with the files in the directories",
            "usage: tree path"
    );

    /**
     * The string used for each level of indentation.
     */
    private static final String INDENTATION = "  ";

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

        if (!Files.isDirectory(path)) {
            env.writeln(parsedArguments.get(0) + " is not a directory.");
            return ShellStatus.CONTINUE;
        }

        try {
            Files.walkFileTree(path, new EnvWriteVisitor(env));
        } catch (IOException e) {
            env.writeln("I/O exception: " + e.toString());
        }

        return ShellStatus.CONTINUE;
    }

    @Override
    public String getCommandName() {
        return "tree";
    }

    @Override
    public List<String> getCommandDescription() {
        return DESCRIPTION;
    }

    /**
     * A visitor used to walk a file tree and print all the files and directories.
     */
    private static class EnvWriteVisitor implements FileVisitor<Path> {
        /**
         * The environment to print to.
         */
        private Environment env;
        /**
         * The current indentation level of the visitor.
         */
        private int level = 0;

        /**
         * Constructs a new visitor for an environment.
         *
         * @param env the environment to write to
         */
        public EnvWriteVisitor(Environment env) {
            this.env = Objects.requireNonNull(env);
        }

        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
            env.writeln(" ".repeat(level * 2) + dir.toAbsolutePath().normalize().getFileName());
            level++;
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
            env.writeln(INDENTATION.repeat(level) + file.getFileName());
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(Path file, IOException exc) {
            return visitFile(file, null);
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
            level--;
            return FileVisitResult.CONTINUE;
        }
    }
}
