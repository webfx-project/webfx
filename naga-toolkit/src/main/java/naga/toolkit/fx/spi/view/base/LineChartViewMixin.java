package naga.toolkit.fx.spi.view.base;

import naga.toolkit.fx.ext.chart.LineChart;
import naga.toolkit.fx.spi.view.LineChartView;

/**
 * @author Bruno Salmon
 */
public interface LineChartViewMixin<C>

        extends LineChartView,
        ChartViewMixin<C, LineChart, LineChartViewBase<C>, LineChartViewMixin<C>> {
}
