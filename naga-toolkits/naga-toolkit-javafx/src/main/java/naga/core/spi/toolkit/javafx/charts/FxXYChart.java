package naga.core.spi.toolkit.javafx.charts;

import javafx.scene.chart.XYChart;
import naga.core.util.Numbers;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public class FxXYChart extends FxChart {

    private List<XYChart.Series<String, Double>> seriesList;
    private XYChart.Series<String, Double> currentSeries;

    public FxXYChart(javafx.scene.chart.Chart chart) {
        super(chart);
    }

    @Override
    protected void createChartData() {
        seriesList = new ArrayList<>();
    }

    @Override
    protected void startSeries(String name) {
        currentSeries = new XYChart.Series<>();
        currentSeries.setName(name);
        seriesList.add(currentSeries);
    }

    @Override
    protected void addValueToCurrentSeries(Object value, String valueName) {
        currentSeries.getData().add(new XYChart.Data<>(valueName, Numbers.doubleValue(value)));
    }

    @Override
    protected void applyChartData() {
        ((XYChart) node).getData().setAll(seriesList);
    }
}