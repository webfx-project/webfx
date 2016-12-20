package naga.toolkit.fx.spi.viewer.base;

import naga.toolkit.fx.ext.chart.PieChart;

/**
 * @author Bruno Salmon
 */
public class PieChartViewerBase
        <C, N extends PieChart, NV extends PieChartViewerBase<C, N, NV, NM>, NM extends PieChartViewerMixin<C, N, NV, NM>>

        extends ChartViewerBase<C, N, NV, NM> {
}
