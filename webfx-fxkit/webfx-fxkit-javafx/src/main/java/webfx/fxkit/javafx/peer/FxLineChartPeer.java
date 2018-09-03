package webfx.fxkit.javafx.peer;

import webfx.fxkits.extra.chart.LineChart;
import webfx.fxkits.extra.spi.peer.base.LineChartPeerBase;
import webfx.fxkits.extra.spi.peer.base.LineChartPeerMixin;

/**
 * @author Bruno Salmon
 */
public class FxLineChartPeer
        <FxN extends javafx.scene.chart.LineChart, N extends LineChart, NB extends LineChartPeerBase<FxN, N, NB, NM>, NM extends LineChartPeerMixin<FxN, N, NB, NM>>

        extends FxXYChartPeer<FxN, N, NB, NM>
        implements LineChartPeerMixin<FxN, N, NB, NM> {

    public FxLineChartPeer() {
        super((NB) new LineChartPeerBase());
    }

    @Override
    protected FxN createFxNode() {
        javafx.scene.chart.LineChart lineChart = new javafx.scene.chart.LineChart(createNumberAxis(), createNumberAxis());
        lineChart.setCreateSymbols(false);
        return (FxN) lineChart;
    }
}
