package webfx.fxkit.extra.controls.mapper.spi.peer.impl.base;

import webfx.fxkit.extra.controls.displaydata.chart.LineChart;

/**
 * @author Bruno Salmon
 */
public final class LineChartPeerBase
        <C, N extends LineChart, NB extends LineChartPeerBase<C, N, NB, NM>, NM extends LineChartPeerMixin<C, N, NB, NM>>

        extends ChartPeerBase<C, N, NB, NM> {
}
