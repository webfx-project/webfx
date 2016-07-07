package naga.toolkit.providers.javafx.nodes.charts;

import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.Chart;
import javafx.scene.chart.NumberAxis;
import naga.toolkit.spi.nodes.charts.ScatterChart;

/**
 * @author Bruno Salmon
 */
public class FxScatterChart extends FxXYChart implements ScatterChart<Chart> {

    public FxScatterChart() {
        this(createScatterChart());
    }

    public FxScatterChart(javafx.scene.chart.ScatterChart scatterChart) {
        super(scatterChart);
    }

    private static javafx.scene.chart.ScatterChart createScatterChart() {
        return new javafx.scene.chart.ScatterChart(new CategoryAxis(), new NumberAxis());
    }
}
