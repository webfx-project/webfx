package naga.toolkit.fx.spi.view.base;

import naga.toolkit.fx.ext.chart.BarChart;
import naga.toolkit.fx.spi.view.BarChartView;

/**
 * @author Bruno Salmon
 */
public interface BarChartViewMixin<C>

        extends BarChartView,
        ChartViewMixin<C, BarChart, BarChartViewBase<C>, BarChartViewMixin<C>> {
}
