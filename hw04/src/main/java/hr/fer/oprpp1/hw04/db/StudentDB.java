package hr.fer.oprpp1.hw04.db;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Main database program
 *
 * @author Borna Cafuk
 */
public class StudentDB {
    /**
     * The pattern used to match commands
     */
    private static final Pattern COMMAND_PATTERN = Pattern.compile("(\\S+)(.*)");
    /**
     * The file from which to read
     */
    private static final String DB_PATH = "database.txt";

    /**
     * The database object to use.
     */
    private static StudentDatabase db;

    public static void main(String[] args) {
        try {
            List<String> lines = Files.lines(Path.of(DB_PATH)).collect(Collectors.toList());
            db = new StudentDatabase(lines.toArray(String[]::new));
        } catch (IOException e) {
            System.err.println("Error opening file " + DB_PATH);
            return;
        } catch (IllegalArgumentException e) {
            System.err.println("Error parsing database records " + e.getMessage());
            return;
        }

        Scanner input = new Scanner(System.in);

        inputLoop:
        while (true) {
            System.out.print("> ");

            if (!input.hasNextLine())
                break;

            String command = input.nextLine();
            Matcher m = COMMAND_PATTERN.matcher(command);

            if (!m.matches()) {
                System.out.println("Enter a command");
                continue;
            }

            String commandName = m.group(1);
            String param = m.group(2);

            switch (commandName) {
                case "query" -> query(param);
                case "exit" -> {
                    break inputLoop;
                }
                default -> System.out.println("Unknown command " + commandName);
            }
        }

        System.out.println("Goodbye!");
    }

    /**
     * Queries the database and prints the result.
     *
     * @param query the query string to use
     */
    private static void query(String query) {
        if (query == null || query.isBlank()) {
            System.out.println("Missing query.");
            return;
        }

        List<StudentRecord> records;
        try {
            records = getRecords(query);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid query: " + e.getMessage());
            return;
        }

        if (!records.isEmpty())
            RecordFormatter.format(records).forEach(System.out::println);
        System.out.printf("Records selected: %d%n%n", records.size());
    }

    /**
     * Queries the database and returns the records.
     *
     * @param queryText the query string to use
     * @return the records that satisfy the query
     */
    private static List<StudentRecord> getRecords(String queryText) {
        QueryParser parser = new QueryParser(queryText);

        if (parser.isDirectQuery()) {
            System.out.println("Using index for record retrieval.");
            StudentRecord record = db.forJMBAG(parser.getQueriedJMBAG());

            if (record == null)
                return List.of();
            else
                return List.of(record);
        } else {
            return db.filter(new QueryFilter(parser.getQuery()));
        }
    }
}
