package naga.toolkit.fx.spi.viewer.base;

import naga.toolkit.fx.ext.chart.AreaChart;
import naga.toolkit.fx.spi.viewer.AreaChartViewer;

/**
 * @author Bruno Salmon
 */
public class AreaChartViewerBase<C>

        extends ChartViewerBase<C, AreaChart, AreaChartViewerBase<C>, AreaChartViewerMixin<C>>
        implements AreaChartViewer {

}
