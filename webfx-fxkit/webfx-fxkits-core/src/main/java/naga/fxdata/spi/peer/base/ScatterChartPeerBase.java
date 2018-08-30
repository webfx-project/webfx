package naga.fxdata.spi.peer.base;

import naga.fxdata.chart.ScatterChart;

/**
 * @author Bruno Salmon
 */
public class ScatterChartPeerBase
        <C, N extends ScatterChart, NB extends ScatterChartPeerBase<C, N, NB, NM>, NM extends ScatterChartPeerMixin<C, N, NB, NM>>

        extends ChartPeerBase<C, N, NB, NM> {
}
