package naga.toolkit.fx.spi.view.base;

import naga.toolkit.fx.ext.chart.LineChart;
import naga.toolkit.fx.spi.view.LineChartView;

/**
 * @author Bruno Salmon
 */
public class LineChartViewBase<C>
        extends ChartViewBase<C, LineChart, LineChartViewBase<C>, LineChartViewMixin<C>>
        implements LineChartView {

}
