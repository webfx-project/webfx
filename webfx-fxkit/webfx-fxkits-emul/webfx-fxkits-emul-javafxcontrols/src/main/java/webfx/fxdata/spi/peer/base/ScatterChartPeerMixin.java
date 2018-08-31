package webfx.fxdata.spi.peer.base;

import webfx.fxdata.chart.ScatterChart;

/**
 * @author Bruno Salmon
 */
public interface ScatterChartPeerMixin
        <C, N extends ScatterChart, NB extends ScatterChartPeerBase<C, N, NB, NM>, NM extends ScatterChartPeerMixin<C, N, NB, NM>>

        extends ChartPeerMixin<C, N, NB, NM> {
}
