package naga.providers.toolkit.javafx.fx.view;

import naga.toolkit.fx.ext.chart.LineChart;
import naga.toolkit.fx.spi.view.base.LineChartViewBase;
import naga.toolkit.fx.spi.view.base.LineChartViewMixin;

/**
 * @author Bruno Salmon
 */
public class FxLineChartView
        extends FxXYChartView<javafx.scene.chart.LineChart, LineChart, LineChartViewBase<javafx.scene.chart.LineChart>, LineChartViewMixin<javafx.scene.chart.LineChart>>
        implements LineChartViewMixin<javafx.scene.chart.LineChart> {

    public FxLineChartView() {
        super(new LineChartViewBase<>());
    }

    @Override
    javafx.scene.chart.LineChart createFxNode() {
        javafx.scene.chart.LineChart lineChart = new javafx.scene.chart.LineChart(createNumberAxis(), createNumberAxis());
        lineChart.setCreateSymbols(false);
        return lineChart;
    }
}
