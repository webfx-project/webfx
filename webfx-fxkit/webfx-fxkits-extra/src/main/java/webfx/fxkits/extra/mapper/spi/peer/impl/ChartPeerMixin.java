package webfx.fxkits.extra.mapper.spi.peer.impl;

import webfx.fxkits.extra.displaydata.DisplayResult;
import webfx.fxkits.extra.type.Type;
import webfx.platforms.core.util.function.Function;
import webfx.fxkits.extra.chart.Chart;

/**
 * @author Bruno Salmon
 */
public interface ChartPeerMixin
        <C, N extends Chart, NB extends ChartPeerBase<C, N, NB, NM>, NM extends ChartPeerMixin<C, N, NB, NM>>

        extends SelectableDisplayResultControlPeerMixin<C, N, NB, NM> {

    @Override
    default void updateResult(DisplayResult rs) {
        getNodePeerBase().updateResult(rs);
    }

    void createChartData(Type xType, Type yType, int pointPerSeriesCount, int seriesCount, Function<Integer, String> seriesNameGetter);

    void setChartDataX(Object xValue, int pointIndex);

    void setChartDataY(Object yValue, int pointIndex, int seriesIndex);

    void applyChartData();

}
