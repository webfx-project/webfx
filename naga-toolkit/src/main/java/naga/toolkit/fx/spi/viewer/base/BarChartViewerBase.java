package naga.toolkit.fx.spi.viewer.base;

import naga.toolkit.fx.ext.chart.BarChart;
import naga.toolkit.fx.spi.viewer.BarChartViewer;

/**
 * @author Bruno Salmon
 */
public class BarChartViewerBase<C>
        extends ChartViewerBase<C, BarChart, BarChartViewerBase<C>, BarChartViewerMixin<C>>
        implements BarChartViewer {

}
