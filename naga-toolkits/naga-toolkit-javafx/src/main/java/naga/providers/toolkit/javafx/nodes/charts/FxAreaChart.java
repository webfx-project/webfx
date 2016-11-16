package naga.providers.toolkit.javafx.nodes.charts;

import naga.toolkit.spi.nodes.charts.AreaChart;

/**
 * @author Bruno Salmon
 */
public class FxAreaChart extends FxXYChart<javafx.scene.chart.AreaChart> implements AreaChart {

    public FxAreaChart() {
        this(createAreaChart());
    }

    public FxAreaChart(javafx.scene.chart.AreaChart lineChart) {
        super(lineChart);
    }

    private static javafx.scene.chart.AreaChart createAreaChart() {
        javafx.scene.chart.AreaChart<Number, Number> areaChart = new javafx.scene.chart.AreaChart<>(createNumberAxis(), createNumberAxis());
        areaChart.setCreateSymbols(false);
        return areaChart;
    }
}