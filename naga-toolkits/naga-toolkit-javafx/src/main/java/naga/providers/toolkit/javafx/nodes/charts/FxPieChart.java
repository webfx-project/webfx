package naga.providers.toolkit.javafx.nodes.charts;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import naga.commons.type.Type;
import naga.commons.util.Numbers;
import naga.commons.util.function.Function;
import naga.toolkit.spi.nodes.charts.PieChart;

/**
 * @author Bruno Salmon
 */
public class FxPieChart extends FxChart<javafx.scene.chart.PieChart> implements PieChart<javafx.scene.chart.PieChart> {

    private ObservableList<javafx.scene.chart.PieChart.Data> pieData;
    private Function<Integer, String> seriesNameGetter;

    public FxPieChart() {
        this(createPieChart());
    }

    public FxPieChart(javafx.scene.chart.PieChart pieChart) {
        super(pieChart);
    }

    private static javafx.scene.chart.PieChart createPieChart() {
        javafx.scene.chart.PieChart pieChart = new javafx.scene.chart.PieChart();
        pieChart.setStartAngle(90);
        return pieChart;
    }

    @Override
    protected void createChartData(Type xType, Type yType, int pointPerSeriesCount, int seriesCount, Function<Integer, String> seriesNameGetter) {
        pieData = FXCollections.observableArrayList();
        this.seriesNameGetter = seriesNameGetter;
    }

    @Override
    protected void setChartDataX(Object xValue, int pointIndex) {
    }

    @Override
    protected void setChartDataY(Object yValue, int pointIndex, int seriesIndex) {
        pieData.add(new javafx.scene.chart.PieChart.Data(seriesNameGetter.apply(seriesIndex), Numbers.doubleValue(yValue)));
    }

    @Override
    protected void applyChartData() {
        node.setData(pieData);
    }
}
