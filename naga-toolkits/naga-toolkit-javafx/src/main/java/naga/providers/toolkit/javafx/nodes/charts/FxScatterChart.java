package naga.providers.toolkit.javafx.nodes.charts;

import naga.toolkit.spi.nodes.charts.ScatterChart;

/**
 * @author Bruno Salmon
 */
public class FxScatterChart extends FxXYChart<javafx.scene.chart.ScatterChart> implements ScatterChart<javafx.scene.chart.ScatterChart> {

    public FxScatterChart() {
        this(createScatterChart());
    }

    public FxScatterChart(javafx.scene.chart.ScatterChart scatterChart) {
        super(scatterChart);
    }

    private static javafx.scene.chart.ScatterChart createScatterChart() {
        return new javafx.scene.chart.ScatterChart(createNumberAxis(), createNumberAxis());
    }
}
