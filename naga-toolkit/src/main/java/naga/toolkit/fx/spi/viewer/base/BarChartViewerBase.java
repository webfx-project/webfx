package naga.toolkit.fx.spi.viewer.base;

import naga.toolkit.fx.ext.chart.BarChart;

/**
 * @author Bruno Salmon
 */
public class BarChartViewerBase
        <C, N extends BarChart, NV extends BarChartViewerBase<C, N, NV, NM>, NM extends BarChartViewerMixin<C, N, NV, NM>>

        extends ChartViewerBase<C, N, NV, NM> {
}
