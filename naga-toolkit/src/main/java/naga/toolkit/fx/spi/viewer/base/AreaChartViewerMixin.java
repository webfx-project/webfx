package naga.toolkit.fx.spi.viewer.base;

import naga.toolkit.fx.ext.chart.AreaChart;

/**
 * @author Bruno Salmon
 */
public interface AreaChartViewerMixin
        <C, N extends AreaChart, NV extends AreaChartViewerBase<C, N, NV, NM>, NM extends AreaChartViewerMixin<C, N, NV, NM>>

        extends ChartViewerMixin<C, N, NV, NM> {
}
