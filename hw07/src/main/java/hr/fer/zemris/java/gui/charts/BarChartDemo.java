package hr.fer.zemris.java.gui.charts;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

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
    }

    /**
     * Initializes the chart and label components for the window.
     *
     * @param chart    the chart
     * @param filePath the path to display at the top of the window
     * @throws NullPointerException if {@code chart} or {@code filePath} is {@code null}
     */
    private void initGUI(BarChart chart, String filePath) {
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
        }
        String path = args[0];

        BarChart model = new BarChart(
                Arrays.asList(
                        new XYValue(1, 8), new XYValue(2, 20), new XYValue(3, 22),
                        new XYValue(4, 10), new XYValue(5, 4)
                ),
                "Number of people in the car",
                "Frequency",
                0,
                22,
                2
        );

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new BarChartDemo(model, path);
            frame.setVisible(true);
        });
    }
}
