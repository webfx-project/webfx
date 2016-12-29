package naga.fx.spi.javafx.fx.viewer;

import naga.fxdata.chart.BarChart;
import naga.fxdata.spi.viewer.base.BarChartViewerBase;
import naga.fxdata.spi.viewer.base.BarChartViewerMixin;

/**
 * @author Bruno Salmon
 */
public class FxBarChartViewer
        <FxN extends javafx.scene.chart.BarChart, N extends BarChart, NB extends BarChartViewerBase<FxN, N, NB, NM>, NM extends BarChartViewerMixin<FxN, N, NB, NM>>

        extends FxXYChartViewer<FxN, N, NB, NM>
        implements BarChartViewerMixin<FxN, N, NB, NM> {

    public FxBarChartViewer() {
        super((NB) new BarChartViewerBase());
    }

    @Override
    protected FxN createFxNode() {
        return (FxN) new javafx.scene.chart.BarChart(createNumberAxis(), createNumberAxis());
    }
}
