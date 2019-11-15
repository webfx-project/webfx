package webfx.extras.visual.controls.charts.peers.javafx;

import javafx.scene.chart.ScatterChart;
import webfx.extras.visual.controls.charts.VisualScatterChart;
import webfx.extras.visual.controls.charts.peers.base.VisualScatterChartPeerBase;
import webfx.extras.visual.controls.charts.peers.base.VisualScatterChartPeerMixin;

/**
 * @author Bruno Salmon
 */
public final class FxVisualScatterChartPeer
        <FxN extends ScatterChart, N extends VisualScatterChart, NB extends VisualScatterChartPeerBase<FxN, N, NB, NM>, NM extends VisualScatterChartPeerMixin<FxN, N, NB, NM>>

        extends FxVisualXYChartPeer<FxN, N, NB, NM>
        implements VisualScatterChartPeerMixin<FxN, N, NB, NM> {

    public FxVisualScatterChartPeer() {
        super((NB) new VisualScatterChartPeerBase());
    }

    @Override
    protected FxN createFxNode() {
        return (FxN) new ScatterChart(createNumberAxis(), createNumberAxis());
    }
}
