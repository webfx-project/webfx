package webfx.fxkit.extra.mapper.spi.peer.impl.extra;

import webfx.fxkit.extra.controls.displaydata.chart.BarChart;

/**
 * @author Bruno Salmon
 */
public class BarChartPeerBase
        <C, N extends BarChart, NB extends BarChartPeerBase<C, N, NB, NM>, NM extends BarChartPeerMixin<C, N, NB, NM>>

        extends ChartPeerBase<C, N, NB, NM> {
}
