package webfx.fxkit.extra.controls.mapper.spi.peer.impl.base;

import webfx.fxkit.extra.controls.displaydata.chart.AreaChart;

/**
 * @author Bruno Salmon
 */
public interface AreaChartPeerMixin
        <C, N extends AreaChart, NB extends AreaChartPeerBase<C, N, NB, NM>, NM extends AreaChartPeerMixin<C, N, NB, NM>>

        extends ChartPeerMixin<C, N, NB, NM> {
}
