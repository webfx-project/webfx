package naga.providers.toolkit.javafx.nodes.charts;

import naga.toolkit.spi.nodes.charts.BarChart;

/**
 * @author Bruno Salmon
 */
public class FxBarChart extends FxXYChart<javafx.scene.chart.BarChart> implements BarChart<javafx.scene.chart.BarChart> {

    public FxBarChart() {
        this(createBarChart());
    }

    public FxBarChart(javafx.scene.chart.BarChart chart) {
        super(chart);
    }

    private static javafx.scene.chart.BarChart createBarChart() {
        return new javafx.scene.chart.BarChart(createNumberAxis(), createNumberAxis());
    }
}
