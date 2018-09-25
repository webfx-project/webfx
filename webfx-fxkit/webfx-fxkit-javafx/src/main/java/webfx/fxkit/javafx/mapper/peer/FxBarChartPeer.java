package webfx.fxkit.javafx.mapper.peer;

import webfx.fxkits.extra.chart.BarChart;
import webfx.fxkits.extra.mapper.spi.peer.impl.BarChartPeerMixin;
import webfx.fxkits.extra.mapper.spi.peer.impl.BarChartPeerBase;

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
