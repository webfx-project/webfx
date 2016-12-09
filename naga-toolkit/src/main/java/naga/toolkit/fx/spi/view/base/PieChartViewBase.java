package naga.toolkit.fx.spi.view.base;

import naga.toolkit.fx.ext.chart.PieChart;
import naga.toolkit.fx.spi.view.PieChartView;

/**
 * @author Bruno Salmon
 */
public class PieChartViewBase<C>
        extends ChartViewBase<C, PieChart, PieChartViewBase<C>, PieChartViewMixin<C>>
        implements PieChartView {
}
