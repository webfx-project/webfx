package naga.providers.toolkit.javafx.fx.viewer;

import naga.toolkit.fx.ext.chart.BarChart;
import naga.toolkit.fx.spi.viewer.base.BarChartViewerBase;
import naga.toolkit.fx.spi.viewer.base.BarChartViewerMixin;

/**
 * @author Bruno Salmon
 */
public class FxBarChartViewer
        extends FxXYChartViewer<javafx.scene.chart.BarChart, BarChart, BarChartViewerBase<javafx.scene.chart.BarChart>, BarChartViewerMixin<javafx.scene.chart.BarChart>>
        implements BarChartViewerMixin<javafx.scene.chart.BarChart> {

    public FxBarChartViewer() {
        super(new BarChartViewerBase<>());
    }

    @Override
    protected javafx.scene.chart.BarChart createFxNode() {
        return new javafx.scene.chart.BarChart(createNumberAxis(), createNumberAxis());
    }
}
