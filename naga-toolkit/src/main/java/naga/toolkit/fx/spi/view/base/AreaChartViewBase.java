package naga.toolkit.fx.spi.view.base;

import naga.toolkit.fx.ext.chart.AreaChart;
import naga.toolkit.fx.spi.view.AreaChartView;

/**
 * @author Bruno Salmon
 */
public class AreaChartViewBase<C>

        extends ChartViewBase<C, AreaChart, AreaChartViewBase<C>, AreaChartViewMixin<C>>
        implements AreaChartView {

}
