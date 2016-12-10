package naga.providers.toolkit.javafx.fx.viewer;

import naga.toolkit.fx.ext.chart.ScatterChart;
import naga.toolkit.fx.spi.viewer.base.ScatterChartViewerBase;
import naga.toolkit.fx.spi.viewer.base.ScatterChartViewerMixin;

/**
 * @author Bruno Salmon
 */
public class FxScatterChartViewer
        extends FxXYChartViewer<javafx.scene.chart.ScatterChart, ScatterChart, ScatterChartViewerBase<javafx.scene.chart.ScatterChart>, ScatterChartViewerMixin<javafx.scene.chart.ScatterChart>>
        implements ScatterChartViewerMixin<javafx.scene.chart.ScatterChart> {

    public FxScatterChartViewer() {
        super(new ScatterChartViewerBase<>());
    }

    @Override
    javafx.scene.chart.ScatterChart createFxNode() {
        return new javafx.scene.chart.ScatterChart(createNumberAxis(), createNumberAxis());
    }
}
