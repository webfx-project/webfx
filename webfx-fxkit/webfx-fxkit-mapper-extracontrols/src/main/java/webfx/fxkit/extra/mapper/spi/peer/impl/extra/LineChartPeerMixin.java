package webfx.fxkit.extra.mapper.spi.peer.impl.extra;

import webfx.fxkit.extra.controls.displaydata.chart.LineChart;

/**
 * @author Bruno Salmon
 */
public interface LineChartPeerMixin
        <C, N extends LineChart, NB extends LineChartPeerBase<C, N, NB, NM>, NM extends LineChartPeerMixin<C, N, NB, NM>>

        extends ChartPeerMixin<C, N, NB, NM> {
}
