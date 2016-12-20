package naga.toolkit.fx.spi.viewer.base;

import naga.toolkit.fx.ext.chart.AreaChart;

/**
 * @author Bruno Salmon
 */
public interface AreaChartViewerMixin
        <C, N extends AreaChart, NB extends AreaChartViewerBase<C, N, NB, NM>, NM extends AreaChartViewerMixin<C, N, NB, NM>>

        extends ChartViewerMixin<C, N, NB, NM> {
}
