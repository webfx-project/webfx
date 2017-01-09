package naga.fxdata.spi.peer.base;

import naga.fxdata.chart.LineChart;

/**
 * @author Bruno Salmon
 */
public class LineChartPeerBase
        <C, N extends LineChart, NB extends LineChartPeerBase<C, N, NB, NM>, NM extends LineChartPeerMixin<C, N, NB, NM>>

        extends ChartPeerBase<C, N, NB, NM> {
}
