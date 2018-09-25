package webfx.fxkit.javafx.mapper.peer;

import webfx.fxkits.extra.chart.ScatterChart;
import webfx.fxkits.extra.mapper.spi.peer.impl.ScatterChartPeerBase;
import webfx.fxkits.extra.mapper.spi.peer.impl.ScatterChartPeerMixin;

/**
 * @author Bruno Salmon
 */
public final class FxScatterChartPeer
        <FxN extends javafx.scene.chart.ScatterChart, N extends ScatterChart, NB extends ScatterChartPeerBase<FxN, N, NB, NM>, NM extends ScatterChartPeerMixin<FxN, N, NB, NM>>

        extends FxXYChartPeer<FxN, N, NB, NM>
        implements ScatterChartPeerMixin<FxN, N, NB, NM> {

    public FxScatterChartPeer() {
        super((NB) new ScatterChartPeerBase());
    }

    @Override
    protected FxN createFxNode() {
        return (FxN) new javafx.scene.chart.ScatterChart(createNumberAxis(), createNumberAxis());
    }
}
