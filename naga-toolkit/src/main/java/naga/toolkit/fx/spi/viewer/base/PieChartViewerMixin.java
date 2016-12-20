package naga.toolkit.fx.spi.viewer.base;

import naga.toolkit.fx.ext.chart.PieChart;

/**
 * @author Bruno Salmon
 */
public interface PieChartViewerMixin
        <C, N extends PieChart, NV extends PieChartViewerBase<C, N, NV, NM>, NM extends PieChartViewerMixin<C, N, NV, NM>>

        extends ChartViewerMixin<C, N, NV, NM> {
}
