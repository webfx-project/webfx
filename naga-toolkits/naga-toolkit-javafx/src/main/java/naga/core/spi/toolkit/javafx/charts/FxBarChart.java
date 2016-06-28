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
        this(new javafx.scene.chart.BarChart(new CategoryAxis(), new NumberAxis()));
    }

    public FxBarChart(javafx.scene.chart.BarChart chart) {
        super(chart);
    }
}
