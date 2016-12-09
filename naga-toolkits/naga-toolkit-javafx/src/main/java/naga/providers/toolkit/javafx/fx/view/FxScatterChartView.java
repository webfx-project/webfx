package naga.providers.toolkit.javafx.fx.view;

import naga.toolkit.fx.ext.chart.ScatterChart;
import naga.toolkit.fx.spi.view.base.ScatterChartViewBase;
import naga.toolkit.fx.spi.view.base.ScatterChartViewMixin;

/**
 * @author Bruno Salmon
 */
public class FxScatterChartView
        extends FxXYChartView<javafx.scene.chart.ScatterChart, ScatterChart, ScatterChartViewBase<javafx.scene.chart.ScatterChart>, ScatterChartViewMixin<javafx.scene.chart.ScatterChart>>
        implements ScatterChartViewMixin<javafx.scene.chart.ScatterChart> {

    public FxScatterChartView() {
        super(new ScatterChartViewBase<>());
    }

    @Override
    javafx.scene.chart.ScatterChart createFxNode() {
        return new javafx.scene.chart.ScatterChart(createNumberAxis(), createNumberAxis());
    }
}
