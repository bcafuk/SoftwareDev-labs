package hr.fer.oprpp1.hw05.shell;

import hr.fer.oprpp1.hw05.shell.commands.*;

import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A command line shell.
 *
 * @author Borna Cafuk
 */
public class MyShell {
    /**
     * A message printed when the shell is started.
     */
    private static final String GREETING_MESSAGE = "Welcome to MyShell v 1.0";
    /**
     * The message used when the user requests a command which does not exist.
     */
    private static final String NO_SUCH_COMMAND = "Unknown command: ";

    /**
     * A pattern used to find the end of the command name.
     */
    private static final Pattern WHITESPACE_PATTERN = Pattern.compile("\\s+");

    /**
     * The main program.
     *
     * @param args command line arguments; unused
     */
    public static void main(String[] args) {
        SortedMap<String, ShellCommand> commands = new TreeMap<>();
        registerCommand(commands, new ExitShellCommand());
        registerCommand(commands, new CharsetsShellCommand());
        // TODO: Implement commands.

        Environment env = new StreamEnvironment(System.in, System.out, commands);

        env.writeln(GREETING_MESSAGE);

        while (true) {
            String input = promptAndRead(env).trim();

            if (input.isEmpty())
                continue;

            Matcher m = WHITESPACE_PATTERN.matcher(input);

            String commandName;
            String arguments;

            if (m.find()) {
                commandName = input.substring(0, m.start());
                arguments = input.substring(m.end());
            } else {
                commandName = input;
                arguments = "";
            }

            ShellCommand command = commands.get(commandName);

            if (command == null) {
                env.writeln(NO_SUCH_COMMAND + commandName);
                continue;
            }

            ShellStatus status;
            try {
                status = command.executeCommand(env, arguments);
            } catch (ShellIOException e) {
                break;
            }

            if (status == ShellStatus.TERMINATE)
                break;
        }
    }

    /**
     * Prints the correct prompt characters and reads one or more lines of input from the environment.
     * <p>
     * All of the lines read are concatenated into one string without any newline characters.
     *
     * @param env the environment to read from
     * @return the read string
     */
    private static String promptAndRead(Environment env) {
        StringBuilder sb = new StringBuilder();

        env.write(env.getPromptSymbol() + " ");
        while (true) {
            String line = env.readLine();

            if (line.isEmpty() || line.charAt(line.length() - 1) != env.getMorelinesSymbol()) {
                sb.append(line);
                break;
            }

            sb.append(line, 0, line.length() - 1);
            env.write(env.getMultilineSymbol() + " ");
        }

        return sb.toString();
    }

    /**
     * Puts a new command into a map using {@link ShellCommand#getCommandName()} as the key.
     *
     * @param commands   the map of commands to put the new command into
     * @param newCommand the new command to put into the map
     */
    private static void registerCommand(SortedMap<String, ShellCommand> commands, ShellCommand newCommand) {
        commands.put(newCommand.getCommandName(), newCommand);
    }
}
