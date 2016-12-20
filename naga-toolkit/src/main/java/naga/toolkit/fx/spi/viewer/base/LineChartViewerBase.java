package naga.toolkit.fx.spi.viewer.base;

import naga.toolkit.fx.ext.chart.LineChart;

/**
 * @author Bruno Salmon
 */
public class LineChartViewerBase
        <C, N extends LineChart, NV extends LineChartViewerBase<C, N, NV, NM>, NM extends LineChartViewerMixin<C, N, NV, NM>>

        extends ChartViewerBase<C, N, NV, NM> {
}
