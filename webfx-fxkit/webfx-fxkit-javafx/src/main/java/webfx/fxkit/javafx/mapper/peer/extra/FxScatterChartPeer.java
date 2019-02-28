package webfx.fxkit.javafx.mapper.peer.extra;

import webfx.fxkit.extra.controls.displaydata.chart.ScatterChart;
import webfx.fxkit.extra.mapper.spi.peer.impl.extra.ScatterChartPeerBase;
import webfx.fxkit.extra.mapper.spi.peer.impl.extra.ScatterChartPeerMixin;

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
