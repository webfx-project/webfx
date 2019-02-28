package webfx.fxkit.javafx.mapper.peer.extra;

import webfx.fxkit.extra.controls.displaydata.chart.LineChart;
import webfx.fxkit.extra.mapper.spi.peer.impl.extra.LineChartPeerBase;
import webfx.fxkit.extra.mapper.spi.peer.impl.extra.LineChartPeerMixin;

/**
 * @author Bruno Salmon
 */
public final class FxLineChartPeer
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
