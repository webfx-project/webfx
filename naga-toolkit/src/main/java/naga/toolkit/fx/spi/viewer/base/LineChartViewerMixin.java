package naga.toolkit.fx.spi.viewer.base;

import naga.toolkit.fx.ext.chart.LineChart;

/**
 * @author Bruno Salmon
 */
public interface LineChartViewerMixin
        <C, N extends LineChart, NV extends LineChartViewerBase<C, N, NV, NM>, NM extends LineChartViewerMixin<C, N, NV, NM>>

        extends ChartViewerMixin<C, N, NV, NM> {
}
