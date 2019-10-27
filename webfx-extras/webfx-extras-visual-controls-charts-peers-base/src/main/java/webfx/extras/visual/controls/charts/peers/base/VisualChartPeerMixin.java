package webfx.extras.visual.controls.charts.peers.base;

import webfx.extras.visual.controls.charts.VisualChart;
import webfx.extras.visual.controls.peers.base.SelectableVisualResultControlPeerMixin;
import webfx.extras.visual.VisualResult;
import webfx.extras.type.Type;

import java.util.function.Function;

/**
 * @author Bruno Salmon
 */
public interface VisualChartPeerMixin
        <C, N extends VisualChart, NB extends VisualChartPeerBase<C, N, NB, NM>, NM extends VisualChartPeerMixin<C, N, NB, NM>>

        extends SelectableVisualResultControlPeerMixin<C, N, NB, NM> {

    @Override
    default void updateVisualResult(VisualResult rs) {
        getNodePeerBase().updateResult(rs);
    }

    void createChartData(Type xType, Type yType, int pointPerSeriesCount, int seriesCount, Function<Integer, String> seriesNameGetter);

    void setChartDataX(Object xValue, int pointIndex);

    void setChartDataY(Object yValue, int pointIndex, int seriesIndex);

    void applyChartData();

}
