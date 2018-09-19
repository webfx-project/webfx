package webfx.fxkits.extra.mapper.spi.peer.impl;

import webfx.fxkits.extra.chart.PieChart;

/**
 * @author Bruno Salmon
 */
public interface PieChartPeerMixin
        <C, N extends PieChart, NB extends PieChartPeerBase<C, N, NB, NM>, NM extends PieChartPeerMixin<C, N, NB, NM>>

        extends ChartPeerMixin<C, N, NB, NM> {
}
