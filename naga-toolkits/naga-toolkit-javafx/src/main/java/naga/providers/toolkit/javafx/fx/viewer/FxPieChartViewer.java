package naga.providers.toolkit.javafx.fx.viewer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import naga.commons.type.Type;
import naga.commons.util.Numbers;
import naga.commons.util.function.Function;
import naga.toolkit.fx.ext.chart.PieChart;
import naga.toolkit.fx.spi.viewer.base.PieChartViewerBase;
import naga.toolkit.fx.spi.viewer.base.PieChartViewerMixin;

/**
 * @author Bruno Salmon
 */
public class FxPieChartViewer
        <FxN extends javafx.scene.chart.PieChart, N extends PieChart, NB extends PieChartViewerBase<FxN, N, NB, NM>, NM extends PieChartViewerMixin<FxN, N, NB, NM>>

        extends FxChartViewer<FxN, N, NB, NM>
        implements PieChartViewerMixin<FxN, N, NB, NM> {

    private ObservableList<javafx.scene.chart.PieChart.Data> pieData;
    private Function<Integer, String> seriesNameGetter;

    public FxPieChartViewer() {
        super((NB) new PieChartViewerBase());
    }

    @Override
    protected FxN createFxNode() {
        javafx.scene.chart.PieChart pieChart = new javafx.scene.chart.PieChart();
        pieChart.setStartAngle(90);
        return (FxN) pieChart;
    }

    @Override
    public void createChartData(Type xType, Type yType, int pointPerSeriesCount, int seriesCount, Function<Integer, String> seriesNameGetter) {
        pieData = FXCollections.observableArrayList();
        this.seriesNameGetter = seriesNameGetter;
    }

    @Override
    public void setChartDataX(Object xValue, int pointIndex) {
    }

    @Override
    public void setChartDataY(Object yValue, int pointIndex, int seriesIndex) {
        pieData.add(new javafx.scene.chart.PieChart.Data(seriesNameGetter.apply(seriesIndex), Numbers.doubleValue(yValue)));
    }

    @Override
    public void applyChartData() {
        getFxNode().setData(pieData);
    }
}
