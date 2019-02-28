package webfx.fxkit.javafx.mapper.peer.extra;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import webfx.fxkit.extra.type.Type;
import webfx.platform.shared.util.Numbers;
import java.util.function.Function;
import webfx.fxkit.extra.controls.displaydata.chart.PieChart;
import webfx.fxkit.extra.mapper.spi.peer.impl.extra.PieChartPeerMixin;
import webfx.fxkit.extra.mapper.spi.peer.impl.extra.PieChartPeerBase;

/**
 * @author Bruno Salmon
 */
public final class FxPieChartPeer
        <FxN extends javafx.scene.chart.PieChart, N extends PieChart, NB extends PieChartPeerBase<FxN, N, NB, NM>, NM extends PieChartPeerMixin<FxN, N, NB, NM>>

        extends FxChartPeer<FxN, N, NB, NM>
        implements PieChartPeerMixin<FxN, N, NB, NM> {

    private ObservableList<javafx.scene.chart.PieChart.Data> pieData;
    private Function<Integer, String> seriesNameGetter;

    public FxPieChartPeer() {
        super((NB) new PieChartPeerBase());
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
