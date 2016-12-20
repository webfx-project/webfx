package naga.providers.toolkit.javafx.fx.viewer;

import naga.toolkit.fx.ext.chart.LineChart;
import naga.toolkit.fx.spi.viewer.base.LineChartViewerBase;
import naga.toolkit.fx.spi.viewer.base.LineChartViewerMixin;

/**
 * @author Bruno Salmon
 */
public class FxLineChartViewer
        <FxN extends javafx.scene.chart.LineChart, N extends LineChart, NV extends LineChartViewerBase<FxN, N, NV, NM>, NM extends LineChartViewerMixin<FxN, N, NV, NM>>

        extends FxXYChartViewer<FxN, N, NV, NM>
        implements LineChartViewerMixin<FxN, N, NV, NM> {

    public FxLineChartViewer() {
        super((NV) new LineChartViewerBase());
    }

    @Override
    protected FxN createFxNode() {
        javafx.scene.chart.LineChart lineChart = new javafx.scene.chart.LineChart(createNumberAxis(), createNumberAxis());
        lineChart.setCreateSymbols(false);
        return (FxN) lineChart;
    }
}
