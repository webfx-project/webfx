package webfx.fxkit.extra.mapper.spi.peer.impl;

import webfx.fxkit.extra.chart.AreaChart;

/**
 * @author Bruno Salmon
 */
public interface AreaChartPeerMixin
        <C, N extends AreaChart, NB extends AreaChartPeerBase<C, N, NB, NM>, NM extends AreaChartPeerMixin<C, N, NB, NM>>

        extends ChartPeerMixin<C, N, NB, NM> {
}
