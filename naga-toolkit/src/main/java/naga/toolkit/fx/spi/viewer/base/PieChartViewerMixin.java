package naga.toolkit.fx.spi.viewer.base;

import naga.toolkit.fx.ext.chart.PieChart;
import naga.toolkit.fx.spi.viewer.PieChartViewer;

/**
 * @author Bruno Salmon
 */
public interface PieChartViewerMixin<C>

        extends PieChartViewer,
        ChartViewerMixin<C, PieChart, PieChartViewerBase<C>, PieChartViewerMixin<C>> {
}
