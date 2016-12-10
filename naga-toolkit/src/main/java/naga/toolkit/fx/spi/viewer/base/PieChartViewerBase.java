package naga.toolkit.fx.spi.viewer.base;

import naga.toolkit.fx.ext.chart.PieChart;
import naga.toolkit.fx.spi.viewer.PieChartViewer;

/**
 * @author Bruno Salmon
 */
public class PieChartViewerBase<C>
        extends ChartViewerBase<C, PieChart, PieChartViewerBase<C>, PieChartViewerMixin<C>>
        implements PieChartViewer {
}
