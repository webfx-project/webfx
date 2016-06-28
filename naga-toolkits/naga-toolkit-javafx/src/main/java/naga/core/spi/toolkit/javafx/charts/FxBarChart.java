package naga.core.spi.toolkit.javafx.charts;

import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.Chart;
import javafx.scene.chart.NumberAxis;
import naga.core.spi.toolkit.charts.BarChart;

/**
 * @author Bruno Salmon
 */
public class FxBarChart extends FxXYChart implements BarChart<Chart> {

    public FxBarChart() {
        this(createBarChart());
    }

    public FxBarChart(javafx.scene.chart.BarChart chart) {
        super(chart);
    }

    private static javafx.scene.chart.BarChart createBarChart() {
        return new javafx.scene.chart.BarChart(new CategoryAxis(), new NumberAxis());
    }
}
