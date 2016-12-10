package naga.toolkit.fx.spi.viewer.base;

import naga.toolkit.fx.ext.chart.ScatterChart;
import naga.toolkit.fx.spi.viewer.ScatterChartViewer;

/**
 * @author Bruno Salmon
 */
public interface ScatterChartViewerMixin<C>

        extends ScatterChartViewer,
        ChartViewerMixin<C, ScatterChart, ScatterChartViewerBase<C>, ScatterChartViewerMixin<C>> {
}
