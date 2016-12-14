package naga.providers.toolkit.javafx.fx.viewer;

import naga.toolkit.fx.ext.chart.AreaChart;
import naga.toolkit.fx.spi.viewer.base.AreaChartViewerBase;
import naga.toolkit.fx.spi.viewer.base.AreaChartViewerMixin;

/**
 * @author Bruno Salmon
 */
public class FxAreaChartViewer
        extends FxXYChartViewer<javafx.scene.chart.AreaChart, AreaChart, AreaChartViewerBase<javafx.scene.chart.AreaChart>, AreaChartViewerMixin<javafx.scene.chart.AreaChart>>
        implements AreaChartViewerMixin<javafx.scene.chart.AreaChart> {

    public FxAreaChartViewer() {
        super(new AreaChartViewerBase<>());
    }

    @Override
    protected javafx.scene.chart.AreaChart createFxNode() {
        javafx.scene.chart.AreaChart<Number, Number> areaChart = new javafx.scene.chart.AreaChart<>(createNumberAxis(), createNumberAxis());
        areaChart.setCreateSymbols(false);
        return areaChart;
    }
}
