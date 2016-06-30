package naga.core.spi.toolkit.javafx.charts;

import javafx.scene.chart.XYChart;
import naga.core.type.Type;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public class FxXYChart extends FxChart {

    private List<XYChart.Series> seriesList;
    private XYChart.Series currentSeries;

    public FxXYChart(javafx.scene.chart.Chart chart) {
        super(chart);
    }

    @Override
    protected void createChartData(int seriesCount, int pointPerSeriesCount, Type xType, Type yType) {
        seriesList = new ArrayList<>();
    }

    @Override
    protected void startSeries(String name) {
        currentSeries = new XYChart.Series<>();
        currentSeries.setName(name);
        seriesList.add(currentSeries);
    }

    @Override
    protected void addPointToCurrentSeries(Object xValue, Object yValue) {
        currentSeries.getData().add(new XYChart.Data<>(xValue, yValue));
    }

    @Override
    protected void applyChartData() {
        ((XYChart) node).getData().setAll(seriesList);
    }
}