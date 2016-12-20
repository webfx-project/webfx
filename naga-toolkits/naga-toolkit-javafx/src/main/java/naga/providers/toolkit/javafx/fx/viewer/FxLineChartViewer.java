package naga.providers.toolkit.javafx.fx.viewer;

import naga.toolkit.fx.ext.chart.LineChart;
import naga.toolkit.fx.spi.viewer.base.LineChartViewerBase;
import naga.toolkit.fx.spi.viewer.base.LineChartViewerMixin;

/**
 * @author Bruno Salmon
 */
public class FxLineChartViewer
        <FxN extends javafx.scene.chart.LineChart, N extends LineChart, NB extends LineChartViewerBase<FxN, N, NB, NM>, NM extends LineChartViewerMixin<FxN, N, NB, NM>>

        extends FxXYChartViewer<FxN, N, NB, NM>
        implements LineChartViewerMixin<FxN, N, NB, NM> {

    public FxLineChartViewer() {
        super((NB) new LineChartViewerBase());
    }

    @Override
    protected FxN createFxNode() {
        javafx.scene.chart.LineChart lineChart = new javafx.scene.chart.LineChart(createNumberAxis(), createNumberAxis());
        lineChart.setCreateSymbols(false);
        return (FxN) lineChart;
    }
}
