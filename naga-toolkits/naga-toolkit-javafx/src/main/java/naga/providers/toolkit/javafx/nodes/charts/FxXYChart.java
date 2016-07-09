package naga.providers.toolkit.javafx.nodes.charts;

import javafx.scene.chart.XYChart;
import naga.commons.type.Type;
import naga.commons.util.function.Function;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public class FxXYChart extends FxChart {

    private List<XYChart.Series> seriesList;
    private Object xValue;

    public FxXYChart(javafx.scene.chart.Chart chart) {
        super(chart);
    }

    @Override
    protected void createChartData(Type xType, Type yType, int pointPerSeriesCount, int seriesCount, Function<Integer, String> seriesNameGetter) {
        seriesList = new ArrayList<>();
        for (int seriesIndex = 0; seriesIndex < seriesCount; seriesIndex++) {
            XYChart.Series series = new XYChart.Series<>();
            series.setName(seriesNameGetter.apply(seriesIndex));
            seriesList.add(series);
        }
    }

    @Override
    protected void setChartDataX(Object xValue, int pointIndex) {
        this.xValue = xValue;
    }

    @Override
    protected void setChartDataY(Object yValue, int pointIndex, int seriesIndex) {
        seriesList.get(seriesIndex).getData().add(new XYChart.Data<>(xValue, yValue));
    }

    @Override
    protected void applyChartData() {
        ((XYChart) node).getData().setAll(seriesList);
    }
}