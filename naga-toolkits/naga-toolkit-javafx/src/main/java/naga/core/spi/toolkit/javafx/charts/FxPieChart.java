package naga.core.spi.toolkit.javafx.charts;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.Chart;
import naga.core.spi.toolkit.charts.PieChart;
import naga.core.util.Numbers;

/**
 * @author Bruno Salmon
 */
public class FxPieChart extends FxChart implements PieChart<Chart> {

    private ObservableList<javafx.scene.chart.PieChart.Data> pieData;
    private String currentSeriesName;

    public FxPieChart() {
        this(createPieChart());
    }

    public FxPieChart(javafx.scene.chart.PieChart pieChart) {
        super(pieChart);
    }

    private static javafx.scene.chart.PieChart createPieChart() {
        return new javafx.scene.chart.PieChart();
    }

    @Override
    protected void createChartData() {
        pieData = FXCollections.observableArrayList();
    }

    @Override
    protected void startSeries(String name) {
        currentSeriesName = name;
    }

    @Override
    protected void addValueToCurrentSeries(Object value, String valueName) {
        pieData.add(new javafx.scene.chart.PieChart.Data(currentSeriesName, Numbers.doubleValue(value)));
    }

    @Override
    protected void applyChartData() {
        ((javafx.scene.chart.PieChart) node).setData(pieData);
    }
}
