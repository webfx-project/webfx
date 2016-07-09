package naga.providers.toolkit.javafx.nodes.charts;

import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.Chart;
import javafx.scene.chart.NumberAxis;
import naga.toolkit.spi.nodes.charts.AreaChart;

/**
 * @author Bruno Salmon
 */
public class FxAreaChart extends FxXYChart implements AreaChart<Chart> {

    public FxAreaChart() {
        this(createAreaChart());
    }

    public FxAreaChart(javafx.scene.chart.AreaChart lineChart) {
        super(lineChart);
    }

    private static javafx.scene.chart.AreaChart createAreaChart() {
        return new javafx.scene.chart.AreaChart(new CategoryAxis(), new NumberAxis());
    }
}