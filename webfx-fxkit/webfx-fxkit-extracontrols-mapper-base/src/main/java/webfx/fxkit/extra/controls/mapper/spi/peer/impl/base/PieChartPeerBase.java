package webfx.fxkit.extra.controls.mapper.spi.peer.impl.base;

import webfx.fxkit.extra.controls.displaydata.chart.PieChart;

/**
 * @author Bruno Salmon
 */
public final class PieChartPeerBase
        <C, N extends PieChart, NB extends PieChartPeerBase<C, N, NB, NM>, NM extends PieChartPeerMixin<C, N, NB, NM>>

        extends ChartPeerBase<C, N, NB, NM> {
}
