package naga.providers.toolkit.javafx.fx.view;

import javafx.scene.chart.NumberAxis;
import naga.toolkit.display.DisplaySelection;
import naga.toolkit.fx.ext.chart.Chart;
import naga.toolkit.fx.spi.DrawingRequester;
import naga.toolkit.fx.spi.view.base.ChartViewBase;
import naga.toolkit.fx.spi.view.base.ChartViewMixin;
import naga.toolkit.properties.markers.SelectionMode;

/**
 * @author Bruno Salmon
 */
abstract class FxChartView
        <FxN extends javafx.scene.chart.Chart, N extends Chart, NV extends ChartViewBase<FxN, N, NV, NM>, NM extends ChartViewMixin<FxN, N, NV, NM>>
        extends FxRegionView<FxN, N, NV, NM>
        implements ChartViewMixin<FxN, N, NV, NM>, FxLayoutMeasurable {

    FxChartView(NV base) {
        super(base);
    }

    @Override
    public void bind(N node, DrawingRequester drawingRequester) {
        super.bind(node, drawingRequester);
        getFxNode().setAnimated(false);
    }

    @Override
    public void updateSelectionMode(SelectionMode mode) {
    }

    @Override
    public void updateDisplaySelection(DisplaySelection selection) {
    }

    static NumberAxis createNumberAxis() {
        NumberAxis axis = new NumberAxis();
        axis.setForceZeroInRange(false);
        return axis;
    }
}
