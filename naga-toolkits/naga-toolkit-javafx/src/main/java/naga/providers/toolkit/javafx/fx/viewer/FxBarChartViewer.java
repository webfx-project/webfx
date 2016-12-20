package naga.providers.toolkit.javafx.fx.viewer;

import naga.toolkit.fx.ext.chart.BarChart;
import naga.toolkit.fx.spi.viewer.base.BarChartViewerBase;
import naga.toolkit.fx.spi.viewer.base.BarChartViewerMixin;

/**
 * @author Bruno Salmon
 */
public class FxBarChartViewer
        <FxN extends javafx.scene.chart.BarChart, N extends BarChart, NV extends BarChartViewerBase<FxN, N, NV, NM>, NM extends BarChartViewerMixin<FxN, N, NV, NM>>

        extends FxXYChartViewer<FxN, N, NV, NM>
        implements BarChartViewerMixin<FxN, N, NV, NM> {

    public FxBarChartViewer() {
        super((NV) new BarChartViewerBase());
    }

    @Override
    protected FxN createFxNode() {
        return (FxN) new javafx.scene.chart.BarChart(createNumberAxis(), createNumberAxis());
    }
}
