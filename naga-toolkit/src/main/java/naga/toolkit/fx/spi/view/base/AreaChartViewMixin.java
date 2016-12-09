package naga.toolkit.fx.spi.view.base;

import naga.toolkit.fx.ext.chart.AreaChart;
import naga.toolkit.fx.spi.view.AreaChartView;

/**
 * @author Bruno Salmon
 */
public interface AreaChartViewMixin<C>

        extends AreaChartView,
        ChartViewMixin<C, AreaChart, AreaChartViewBase<C>, AreaChartViewMixin<C>> {
}
