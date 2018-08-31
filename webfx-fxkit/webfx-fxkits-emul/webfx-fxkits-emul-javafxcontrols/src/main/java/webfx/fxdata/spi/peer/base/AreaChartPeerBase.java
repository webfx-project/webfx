package webfx.fxdata.spi.peer.base;

import webfx.fxdata.chart.AreaChart;

/**
 * @author Bruno Salmon
 */
public class AreaChartPeerBase
        <C, N extends AreaChart, NB extends AreaChartPeerBase<C, N, NB, NM>, NM extends AreaChartPeerMixin<C, N, NB, NM>>

        extends ChartPeerBase<C, N, NB, NM> {
}
