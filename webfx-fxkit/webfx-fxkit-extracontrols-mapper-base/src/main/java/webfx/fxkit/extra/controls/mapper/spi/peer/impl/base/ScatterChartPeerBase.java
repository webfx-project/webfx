package webfx.fxkit.extra.controls.mapper.spi.peer.impl.base;

import webfx.fxkit.extra.controls.displaydata.chart.ScatterChart;

/**
 * @author Bruno Salmon
 */
public final class ScatterChartPeerBase
        <C, N extends ScatterChart, NB extends ScatterChartPeerBase<C, N, NB, NM>, NM extends ScatterChartPeerMixin<C, N, NB, NM>>

        extends ChartPeerBase<C, N, NB, NM> {
}
