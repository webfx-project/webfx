package naga.toolkit.fx.spi.viewer.base;

import naga.toolkit.fx.ext.chart.BarChart;

/**
 * @author Bruno Salmon
 */
public interface BarChartViewerMixin
        <C, N extends BarChart, NB extends BarChartViewerBase<C, N, NB, NM>, NM extends BarChartViewerMixin<C, N, NB, NM>>

        extends ChartViewerMixin<C, N, NB, NM> {
}
