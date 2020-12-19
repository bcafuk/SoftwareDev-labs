package hr.fer.zemris.java.gui.charts;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A program which parses chart data from a file and displays it in a window.
 *
 * @author Borna Cafuk
 */
public class BarChartDemo extends JFrame {
    /**
     * The color of the data bars.
     */
    private static final Color BAR_COLOR = new Color(0xf47748);
    /**
     * The color of the gridlines.
     */
    private static final Color GRIDLINE_COLOR = new Color(0xdcc69e);
    /**
     * The color of the chart axes and labels.
     */
    private static final Color AXIS_COLOR = Color.BLACK;

    private static final Pattern DATA_POINT_PATTERN = Pattern.compile("([+-]?\\d+),(\\d+)");

    /**
     * Creates a new window which displays the given chart, along with the path it was loaded from.
     *
     * @param chart    the chart to draw
     * @param filePath the path from which the chart was loaded
     * @throws NullPointerException if {@code chart} or {@code filePath} is {@code null}
     */
    public BarChartDemo(BarChart chart, String filePath) {
        setSize(800, 600);
        setTitle("Bar Chart");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        initGUI(chart, filePath);
        pack();
    }

    /**
     * Initializes the chart and label components for the window.
     *
     * @param chart    the chart
     * @param filePath the path to display at the top of the window
     * @throws NullPointerException if {@code chart} or {@code filePath} is {@code null}
     */
    private void initGUI(BarChart chart, String filePath) {
        Objects.requireNonNull(chart, "The chart must not be null");
        Objects.requireNonNull(filePath, "The file path must not be null");

        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());

        JLabel pathLabel = new JLabel(filePath);
        cp.add(pathLabel, BorderLayout.PAGE_START);

        BarChartComponent barChart = new BarChartComponent(chart);
        barChart.setForeground(BAR_COLOR);
        barChart.setGridlineColor(GRIDLINE_COLOR);
        barChart.setAxisColor(AXIS_COLOR);
        cp.add(barChart, BorderLayout.CENTER);
    }

    /**
     * Parses chart data from a file according to the format specified in the assignment document and
     * creates a window displaying the chart.
     *
     * @param args the command-line arguments; the first element is expected to contain a path to the data file
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Usage: BarChartDemo path");
            System.exit(1);
            return;
        }
        String pathString = args[0];
        Path path = Path.of(pathString);

        BarChart model;
        try (InputStream fileStream = Files.newInputStream(path)) {
            model = parseFile(fileStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String fullPath = path.toAbsolutePath().toString();

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new BarChartDemo(model, fullPath);
            frame.setVisible(true);
        });
    }

    /**
     * Parses chart data from a stream according to the format specified in the assignment document.
     *
     * @param input the stream to read from
     * @return the parsed chart
     * @throws NullPointerException     if {@code input} is {@code null}
     * @throws NoSuchElementException   if the file has fewer than 6 lines
     * @throws NumberFormatException    if a data point is formatted incorrectly
     * @throws IllegalArgumentException if the {@link BarChart#BarChart(List, String, String, int, int, int)} throws an
     *                                  {@code IllegalArgumentException}
     */
    public static BarChart parseFile(InputStream input) {
        try (Scanner scanner = new Scanner(Objects.requireNonNull(input))) {
            String xAxisLabel = scanner.nextLine();
            String yAxisLabel = scanner.nextLine();

            String[] dataStrings = scanner.nextLine().split(" +");
            List<XYValue> dataPoints = new ArrayList<>();

            for (String dataString : dataStrings) {
                Matcher matcher = DATA_POINT_PATTERN.matcher(dataString);

                if (!matcher.matches())
                    throw new NumberFormatException("Invalid data point format: \"" + dataString + "\"");

                int x = Integer.parseInt(matcher.group(1));
                int y = Integer.parseInt(matcher.group(2));

                dataPoints.add(new XYValue(x, y));
            }

            int yMin = scanner.nextInt();
            int yMax = scanner.nextInt();
            int yStep = scanner.nextInt();

            return new BarChart(dataPoints, xAxisLabel, yAxisLabel, yMin, yMax, yStep);
        }
    }
}
