package webfx.extras.visual.controls.charts.peers.javafx;

import javafx.scene.chart.XYChart;
import webfx.extras.visual.controls.charts.VisualChart;
import webfx.extras.visual.controls.charts.peers.base.VisualChartPeerBase;
import webfx.extras.visual.controls.charts.peers.base.VisualChartPeerMixin;
import webfx.extras.type.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * @author Bruno Salmon
 */
public abstract class FxVisualXYChartPeer
        <FxN extends XYChart, N extends VisualChart, NB extends VisualChartPeerBase<FxN, N, NB, NM>, NM extends VisualChartPeerMixin<FxN, N, NB, NM>>
        extends FxVisualChartPeer<FxN, N, NB, NM> {

    private List<XYChart.Series> seriesList;
    private Object xValue;

    public FxVisualXYChartPeer(NB base) {
        super(base);
    }

    @Override
    public void createChartData(Type xType, Type yType, int pointPerSeriesCount, int seriesCount, Function<Integer, String> seriesNameGetter) {
        seriesList = new ArrayList<>();
        for (int seriesIndex = 0; seriesIndex < seriesCount; seriesIndex++) {
            XYChart.Series series = new XYChart.Series<>();
            series.setName(seriesNameGetter.apply(seriesIndex));
            seriesList.add(series);
        }
    }

    @Override
    public void setChartDataX(Object xValue, int pointIndex) {
        this.xValue = xValue;
    }

    @Override
    public void setChartDataY(Object yValue, int pointIndex, int seriesIndex) {
        seriesList.get(seriesIndex).getData().add(new XYChart.Data<>(xValue, yValue));
    }

    @Override
    public void applyChartData() {
        getFxNode().getData().setAll(seriesList);
    }
}
