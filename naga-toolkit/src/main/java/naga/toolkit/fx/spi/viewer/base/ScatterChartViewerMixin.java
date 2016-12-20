package naga.toolkit.fx.spi.viewer.base;

import naga.toolkit.fx.ext.chart.ScatterChart;

/**
 * @author Bruno Salmon
 */
public interface ScatterChartViewerMixin<C>

        extends ChartViewerMixin<C, ScatterChart, ScatterChartViewerBase<C>, ScatterChartViewerMixin<C>> {
}
