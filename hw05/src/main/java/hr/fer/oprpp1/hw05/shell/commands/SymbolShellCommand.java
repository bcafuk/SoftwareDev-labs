package hr.fer.oprpp1.hw05.shell.commands;

import hr.fer.oprpp1.hw05.shell.ArgumentParser;
import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellStatus;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * A command which prints or changes a shell symbol.
 *
 * @author Borna Cafuk
 * @see Environment#getPromptSymbol()
 * @see Environment#getMultilineSymbol()
 * @see Environment#getMorelinesSymbol()
 */
public class SymbolShellCommand implements ShellCommand {
    private static final List<String> DESCRIPTION = List.of(
            "Changes or prints one of the shell's symbols",
            "usage: symbol symbol_name [new_value]",
            "allowed symbol names are: PROMPT, MULTILINE, MORELINES"
    );

    /**
     * A map of objects which tell how to get and set each symbol.
     */
    private static final Map<String, SymbolGetterSetter> getterSetters = Map.of(
            "PROMPT", new SymbolGetterSetter(Environment::getPromptSymbol, Environment::setPromptSymbol),
            "MULTILINE", new SymbolGetterSetter(Environment::getMultilineSymbol, Environment::setMultilineSymbol),
            "MORELINES", new SymbolGetterSetter(Environment::getMorelinesSymbol, Environment::setMorelinesSymbol)
    );

    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        List<String> parsedArguments;
        try {
            parsedArguments = ArgumentParser.parseAll(arguments);
        } catch (IllegalArgumentException e) {
            env.writeln("Invalid argument format");
            return ShellStatus.CONTINUE;
        }

        if (parsedArguments.size() == 0) {
            env.writeln("Please enter a symbol name to query or set");
            return ShellStatus.CONTINUE;
        }

        String symbolName = parsedArguments.get(0);
        SymbolGetterSetter getterSetter = getterSetters.get(symbolName);

        if (getterSetter == null) {
            env.writeln("Unknown symbol: " + symbolName);
            return ShellStatus.CONTINUE;
        }

        if (parsedArguments.size() == 1) {
            env.writeln("Symbol for " + symbolName + " is '" + getterSetter.getter.apply(env) + "'");
        } else {
            String newValue = parsedArguments.get(1);

            if (newValue.length() != 1) {
                env.writeln("The new symbol must consist of only 1 character, but \"" + newValue + "\" was supplied");
                return ShellStatus.CONTINUE;
            }

            char oldValue = getterSetter.getter.apply(env);

            getterSetter.setter.accept(env, newValue.charAt(0));

            env.writeln("Symbol for " + symbolName + " changed from '" + oldValue + "' to '" + newValue + "'");
        }

        return ShellStatus.CONTINUE;
    }

    @Override
    public String getCommandName() {
        return "symbol";
    }

    @Override
    public List<String> getCommandDescription() {
        return DESCRIPTION;
    }

    /**
     * Holds an object's getter and setter.
     */
    private static class SymbolGetterSetter {
        public final Function<Environment, Character> getter;
        public final BiConsumer<Environment, Character> setter;

        public SymbolGetterSetter(Function<Environment, Character> getter,
                                  BiConsumer<Environment, Character> setter) {
            this.getter = getter;
            this.setter = setter;
        }
    }
}
