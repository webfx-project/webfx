package naga.toolkit.fx.spi.viewer.base;

import naga.toolkit.fx.ext.chart.BarChart;

/**
 * @author Bruno Salmon
 */
public interface BarChartViewerMixin
        <C, N extends BarChart, NV extends BarChartViewerBase<C, N, NV, NM>, NM extends BarChartViewerMixin<C, N, NV, NM>>

        extends ChartViewerMixin<C, N, NV, NM> {
}
