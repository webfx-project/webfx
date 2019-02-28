package webfx.fxkit.javafx.mapper.peer.extra;

import webfx.fxkit.extra.controls.displaydata.chart.BarChart;
import webfx.fxkit.extra.mapper.spi.peer.impl.extra.BarChartPeerMixin;
import webfx.fxkit.extra.mapper.spi.peer.impl.extra.BarChartPeerBase;

/**
 * @author Bruno Salmon
 */
public final class FxBarChartPeer
        <FxN extends javafx.scene.chart.BarChart, N extends BarChart, NB extends BarChartPeerBase<FxN, N, NB, NM>, NM extends BarChartPeerMixin<FxN, N, NB, NM>>

        extends FxXYChartPeer<FxN, N, NB, NM>
        implements BarChartPeerMixin<FxN, N, NB, NM> {

    public FxBarChartPeer() {
        super((NB) new BarChartPeerBase());
    }

    @Override
    protected FxN createFxNode() {
        return (FxN) new javafx.scene.chart.BarChart(createNumberAxis(), createNumberAxis());
    }
}
