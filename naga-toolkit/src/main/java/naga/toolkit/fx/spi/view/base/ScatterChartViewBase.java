package naga.toolkit.fx.spi.view.base;

import naga.toolkit.fx.ext.chart.ScatterChart;
import naga.toolkit.fx.spi.view.ScatterChartView;

/**
 * @author Bruno Salmon
 */
public class ScatterChartViewBase<C>
        extends ChartViewBase<C, ScatterChart, ScatterChartViewBase<C>, ScatterChartViewMixin<C>>
        implements ScatterChartView {
}
