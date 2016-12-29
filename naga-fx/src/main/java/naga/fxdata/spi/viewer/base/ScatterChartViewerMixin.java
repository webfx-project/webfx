package naga.fxdata.spi.viewer.base;

import naga.fxdata.chart.ScatterChart;

/**
 * @author Bruno Salmon
 */
public interface ScatterChartViewerMixin
        <C, N extends ScatterChart, NB extends ScatterChartViewerBase<C, N, NB, NM>, NM extends ScatterChartViewerMixin<C, N, NB, NM>>

        extends ChartViewerMixin<C, N, NB, NM> {
}
