package webfx.fxkit.javafx.mapper.peer.extra;

import javafx.scene.chart.XYChart;
import webfx.fxkit.extra.controls.displaydata.chart.Chart;
import webfx.fxkit.extra.controls.mapper.spi.peer.impl.base.ChartPeerBase;
import webfx.fxkit.extra.controls.mapper.spi.peer.impl.base.ChartPeerMixin;
import webfx.fxkit.extra.type.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * @author Bruno Salmon
 */
public abstract class FxXYChartPeer
        <FxN extends javafx.scene.chart.XYChart, N extends Chart, NB extends ChartPeerBase<FxN, N, NB, NM>, NM extends ChartPeerMixin<FxN, N, NB, NM>>
        extends FxChartPeer<FxN, N, NB, NM> {

    private List<XYChart.Series> seriesList;
    private Object xValue;

    public FxXYChartPeer(NB base) {
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
