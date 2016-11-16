package naga.providers.toolkit.javafx.nodes.charts;

import naga.toolkit.spi.nodes.charts.LineChart;

/**
 * @author Bruno Salmon
 */
public class FxLineChart extends FxXYChart<javafx.scene.chart.LineChart> implements LineChart {

    public FxLineChart() {
        this(createLineChart());
    }

    public FxLineChart(javafx.scene.chart.LineChart lineChart) {
        super(lineChart);
    }

    private static javafx.scene.chart.LineChart createLineChart() {
        javafx.scene.chart.LineChart lineChart = new javafx.scene.chart.LineChart(createNumberAxis(), createNumberAxis());
        lineChart.setCreateSymbols(false);
        return lineChart;
    }
}
