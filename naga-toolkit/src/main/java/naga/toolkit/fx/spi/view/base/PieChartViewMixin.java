package naga.toolkit.fx.spi.view.base;

import naga.toolkit.fx.ext.chart.PieChart;
import naga.toolkit.fx.spi.view.PieChartView;

/**
 * @author Bruno Salmon
 */
public interface PieChartViewMixin<C>

        extends PieChartView,
        ChartViewMixin<C, PieChart, PieChartViewBase<C>, PieChartViewMixin<C>> {
}
