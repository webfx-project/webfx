package webfx.fxkits.extra.spi.peer.base;

import webfx.fxkits.extra.chart.BarChart;

/**
 * @author Bruno Salmon
 */
public interface BarChartPeerMixin
        <C, N extends BarChart, NB extends BarChartPeerBase<C, N, NB, NM>, NM extends BarChartPeerMixin<C, N, NB, NM>>

        extends ChartPeerMixin<C, N, NB, NM> {
}
