package webfx.extras.visual.controls.charts.peers.javafx;

import webfx.extras.visual.controls.charts.VisualAreaChart;
import webfx.extras.visual.controls.charts.peers.base.VisualAreaChartPeerMixin;
import webfx.extras.visual.controls.charts.peers.base.VisualAreaChartPeerBase;

/**
 * @author Bruno Salmon
 */
public final class FxVisualAreaChartPeer
        <FxN extends javafx.scene.chart.AreaChart, N extends VisualAreaChart, NB extends VisualAreaChartPeerBase<FxN, N, NB, NM>, NM extends VisualAreaChartPeerMixin<FxN, N, NB, NM>>

        extends FxVisualXYChartPeer<FxN, N, NB, NM>
        implements VisualAreaChartPeerMixin<FxN, N, NB, NM> {

    public FxVisualAreaChartPeer() {
        super((NB) new VisualAreaChartPeerBase());
    }

    @Override
    protected FxN createFxNode() {
        // The API requires the axis to be defined now whereas we don't know the structure to display yet
        // So assuming category on x axis and number on y axis (is it possible to generify that?)
        javafx.scene.chart.AreaChart<String, Number> areaChart = new javafx.scene.chart.AreaChart<>(createCategoryAxis(), createNumberAxis());
        areaChart.setCreateSymbols(false);
        return (FxN) areaChart;
    }
}
