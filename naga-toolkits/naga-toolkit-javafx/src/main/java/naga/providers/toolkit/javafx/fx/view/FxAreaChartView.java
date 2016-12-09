package naga.providers.toolkit.javafx.fx.view;

import naga.toolkit.fx.ext.chart.AreaChart;
import naga.toolkit.fx.spi.view.base.AreaChartViewBase;
import naga.toolkit.fx.spi.view.base.AreaChartViewMixin;

/**
 * @author Bruno Salmon
 */
public class FxAreaChartView
        extends FxXYChartView<javafx.scene.chart.AreaChart, AreaChart, AreaChartViewBase<javafx.scene.chart.AreaChart>, AreaChartViewMixin<javafx.scene.chart.AreaChart>>
        implements AreaChartViewMixin<javafx.scene.chart.AreaChart> {

    public FxAreaChartView() {
        super(new AreaChartViewBase<>());
    }

    @Override
    javafx.scene.chart.AreaChart createFxNode() {
        javafx.scene.chart.AreaChart<Number, Number> areaChart = new javafx.scene.chart.AreaChart<>(createNumberAxis(), createNumberAxis());
        areaChart.setCreateSymbols(false);
        return areaChart;
    }
}
