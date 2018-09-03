package webfx.fxkit.javafx.peer;

import webfx.fxkits.extra.chart.BarChart;
import webfx.fxkits.extra.spi.peer.base.BarChartPeerMixin;
import webfx.fxkits.extra.spi.peer.base.BarChartPeerBase;

/**
 * @author Bruno Salmon
 */
public class FxBarChartPeer
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
