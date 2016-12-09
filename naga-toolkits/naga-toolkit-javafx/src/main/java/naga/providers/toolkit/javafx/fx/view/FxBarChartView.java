package naga.providers.toolkit.javafx.fx.view;

import naga.toolkit.fx.ext.chart.BarChart;
import naga.toolkit.fx.spi.view.base.BarChartViewBase;
import naga.toolkit.fx.spi.view.base.BarChartViewMixin;

/**
 * @author Bruno Salmon
 */
public class FxBarChartView
        extends FxXYChartView<javafx.scene.chart.BarChart, BarChart, BarChartViewBase<javafx.scene.chart.BarChart>, BarChartViewMixin<javafx.scene.chart.BarChart>>
        implements BarChartViewMixin<javafx.scene.chart.BarChart> {

    public FxBarChartView() {
        super(new BarChartViewBase<>());
    }

    @Override
    javafx.scene.chart.BarChart createFxNode() {
        return new javafx.scene.chart.BarChart(createNumberAxis(), createNumberAxis());
    }
}
