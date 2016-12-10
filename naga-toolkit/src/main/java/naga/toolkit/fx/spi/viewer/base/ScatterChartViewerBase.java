package naga.toolkit.fx.spi.viewer.base;

import naga.toolkit.fx.ext.chart.ScatterChart;
import naga.toolkit.fx.spi.viewer.ScatterChartViewer;

/**
 * @author Bruno Salmon
 */
public class ScatterChartViewerBase<C>
        extends ChartViewerBase<C, ScatterChart, ScatterChartViewerBase<C>, ScatterChartViewerMixin<C>>
        implements ScatterChartViewer {
}
