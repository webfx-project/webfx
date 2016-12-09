package naga.toolkit.fx.spi.view.base;

import naga.toolkit.fx.ext.chart.BarChart;
import naga.toolkit.fx.spi.view.BarChartView;

/**
 * @author Bruno Salmon
 */
public class BarChartViewBase<C>
        extends ChartViewBase<C, BarChart, BarChartViewBase<C>, BarChartViewMixin<C>>
        implements BarChartView {

}
