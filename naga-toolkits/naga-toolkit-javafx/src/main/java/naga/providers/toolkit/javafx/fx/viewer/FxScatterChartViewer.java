package naga.providers.toolkit.javafx.fx.viewer;

import naga.toolkit.fx.ext.chart.ScatterChart;
import naga.toolkit.fx.spi.viewer.base.ScatterChartViewerBase;
import naga.toolkit.fx.spi.viewer.base.ScatterChartViewerMixin;

/**
 * @author Bruno Salmon
 */
public class FxScatterChartViewer
        <FxN extends javafx.scene.chart.ScatterChart, N extends ScatterChart, NV extends ScatterChartViewerBase<FxN, N, NV, NM>, NM extends ScatterChartViewerMixin<FxN, N, NV, NM>>

        extends FxXYChartViewer<FxN, N, NV, NM>
        implements ScatterChartViewerMixin<FxN, N, NV, NM> {

    public FxScatterChartViewer() {
        super((NV) new ScatterChartViewerBase());
    }

    @Override
    protected FxN createFxNode() {
        return (FxN) new javafx.scene.chart.ScatterChart(createNumberAxis(), createNumberAxis());
    }
}
