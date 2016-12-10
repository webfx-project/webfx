package naga.toolkit.fx.spi.viewer.base;

import naga.toolkit.fx.ext.chart.BarChart;
import naga.toolkit.fx.spi.viewer.BarChartViewer;

/**
 * @author Bruno Salmon
 */
public interface BarChartViewerMixin<C>

        extends BarChartViewer,
        ChartViewerMixin<C, BarChart, BarChartViewerBase<C>, BarChartViewerMixin<C>> {
}
