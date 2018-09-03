package webfx.fxkits.extra.spi.peer.base;

import webfx.fxkits.extra.chart.AreaChart;

/**
 * @author Bruno Salmon
 */
public class AreaChartPeerBase
        <C, N extends AreaChart, NB extends AreaChartPeerBase<C, N, NB, NM>, NM extends AreaChartPeerMixin<C, N, NB, NM>>

        extends ChartPeerBase<C, N, NB, NM> {
}
