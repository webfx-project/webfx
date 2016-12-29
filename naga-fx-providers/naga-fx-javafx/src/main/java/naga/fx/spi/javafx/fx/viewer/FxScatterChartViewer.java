package naga.fx.spi.javafx.fx.viewer;

import naga.fxdata.chart.ScatterChart;
import naga.fxdata.spi.viewer.base.ScatterChartViewerBase;
import naga.fxdata.spi.viewer.base.ScatterChartViewerMixin;

/**
 * @author Bruno Salmon
 */
public class FxScatterChartViewer
        <FxN extends javafx.scene.chart.ScatterChart, N extends ScatterChart, NB extends ScatterChartViewerBase<FxN, N, NB, NM>, NM extends ScatterChartViewerMixin<FxN, N, NB, NM>>

        extends FxXYChartViewer<FxN, N, NB, NM>
        implements ScatterChartViewerMixin<FxN, N, NB, NM> {

    public FxScatterChartViewer() {
        super((NB) new ScatterChartViewerBase());
    }

    @Override
    protected FxN createFxNode() {
        return (FxN) new javafx.scene.chart.ScatterChart(createNumberAxis(), createNumberAxis());
    }
}
