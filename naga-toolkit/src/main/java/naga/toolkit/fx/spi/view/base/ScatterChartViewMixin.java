package naga.toolkit.fx.spi.view.base;

import naga.toolkit.fx.ext.chart.ScatterChart;
import naga.toolkit.fx.spi.view.ScatterChartView;

/**
 * @author Bruno Salmon
 */
public interface ScatterChartViewMixin<C>

        extends ScatterChartView,
        ChartViewMixin<C, ScatterChart, ScatterChartViewBase<C>, ScatterChartViewMixin<C>> {
}
