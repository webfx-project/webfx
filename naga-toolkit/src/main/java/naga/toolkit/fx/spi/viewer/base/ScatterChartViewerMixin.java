package naga.toolkit.fx.spi.viewer.base;

import naga.toolkit.fx.ext.chart.ScatterChart;

/**
 * @author Bruno Salmon
 */
public interface ScatterChartViewerMixin
        <C, N extends ScatterChart, NV extends ScatterChartViewerBase<C, N, NV, NM>, NM extends ScatterChartViewerMixin<C, N, NV, NM>>

        extends ChartViewerMixin<C, N, NV, NM> {
}
