package naga.providers.toolkit.javafx.fx.viewer;

import naga.toolkit.fx.ext.chart.LineChart;
import naga.toolkit.fx.spi.viewer.base.LineChartViewerBase;
import naga.toolkit.fx.spi.viewer.base.LineChartViewerMixin;

/**
 * @author Bruno Salmon
 */
public class FxLineChartViewer
        extends FxXYChartViewer<javafx.scene.chart.LineChart, LineChart, LineChartViewerBase<javafx.scene.chart.LineChart>, LineChartViewerMixin<javafx.scene.chart.LineChart>>
        implements LineChartViewerMixin<javafx.scene.chart.LineChart> {

    public FxLineChartViewer() {
        super(new LineChartViewerBase<>());
    }

    @Override
    protected javafx.scene.chart.LineChart createFxNode() {
        javafx.scene.chart.LineChart lineChart = new javafx.scene.chart.LineChart(createNumberAxis(), createNumberAxis());
        lineChart.setCreateSymbols(false);
        return lineChart;
    }
}
