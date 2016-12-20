package naga.toolkit.fx.spi.viewer.base;

import naga.toolkit.fx.ext.chart.AreaChart;

/**
 * @author Bruno Salmon
 */
public class AreaChartViewerBase
        <C, N extends AreaChart, NV extends AreaChartViewerBase<C, N, NV, NM>, NM extends AreaChartViewerMixin<C, N, NV, NM>>

        extends ChartViewerBase<C, N, NV, NM> {
}
