package naga.toolkit.fx.spi.viewer.base;

import naga.toolkit.fx.ext.chart.AreaChart;
import naga.toolkit.fx.spi.viewer.AreaChartViewer;

/**
 * @author Bruno Salmon
 */
public interface AreaChartViewerMixin<C>

        extends AreaChartViewer,
        ChartViewerMixin<C, AreaChart, AreaChartViewerBase<C>, AreaChartViewerMixin<C>> {
}
