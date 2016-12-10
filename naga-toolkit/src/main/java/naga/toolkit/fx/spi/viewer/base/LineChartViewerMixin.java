package naga.toolkit.fx.spi.viewer.base;

import naga.toolkit.fx.ext.chart.LineChart;
import naga.toolkit.fx.spi.viewer.LineChartViewer;

/**
 * @author Bruno Salmon
 */
public interface LineChartViewerMixin<C>

        extends LineChartViewer,
        ChartViewerMixin<C, LineChart, LineChartViewerBase<C>, LineChartViewerMixin<C>> {
}
