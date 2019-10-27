package webfx.extras.visual.controls.charts.peers.javafx;

import webfx.extras.visual.controls.charts.VisualLineChart;
import webfx.extras.visual.controls.charts.peers.base.VisualLineChartPeerBase;
import webfx.extras.visual.controls.charts.peers.base.VisualLineChartPeerMixin;

/**
 * @author Bruno Salmon
 */
public final class FxVisualLineChartPeer
        <FxN extends javafx.scene.chart.LineChart, N extends VisualLineChart, NB extends VisualLineChartPeerBase<FxN, N, NB, NM>, NM extends VisualLineChartPeerMixin<FxN, N, NB, NM>>

        extends FxVisualXYChartPeer<FxN, N, NB, NM>
        implements VisualLineChartPeerMixin<FxN, N, NB, NM> {

    public FxVisualLineChartPeer() {
        super((NB) new VisualLineChartPeerBase());
    }

    @Override
    protected FxN createFxNode() {
        javafx.scene.chart.LineChart lineChart = new javafx.scene.chart.LineChart(createNumberAxis(), createNumberAxis());
        lineChart.setCreateSymbols(false);
        return (FxN) lineChart;
    }
}
