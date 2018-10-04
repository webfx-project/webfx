package webfx.fxkit.extra.mapper.spi.peer.impl;

import webfx.fxkit.extra.chart.AreaChart;

/**
 * @author Bruno Salmon
 */
public class AreaChartPeerBase
        <C, N extends AreaChart, NB extends AreaChartPeerBase<C, N, NB, NM>, NM extends AreaChartPeerMixin<C, N, NB, NM>>

        extends ChartPeerBase<C, N, NB, NM> {
}
