package naga.fx.spi.javafx.fx.viewer;

import javafx.scene.chart.NumberAxis;
import naga.fxdata.displaydata.DisplaySelection;
import naga.fxdata.chart.Chart;
import naga.fx.scene.SceneRequester;
import naga.fxdata.spi.viewer.base.ChartViewerBase;
import naga.fxdata.spi.viewer.base.ChartViewerMixin;
import naga.fxdata.displaydata.SelectionMode;

/**
 * @author Bruno Salmon
 */
abstract class FxChartViewer
        <FxN extends javafx.scene.chart.Chart, N extends Chart, NB extends ChartViewerBase<FxN, N, NB, NM>, NM extends ChartViewerMixin<FxN, N, NB, NM>>
        extends FxRegionViewer<FxN, N, NB, NM>
        implements ChartViewerMixin<FxN, N, NB, NM>, FxLayoutMeasurable {

    FxChartViewer(NB base) {
        super(base);
    }

    @Override
    public void bind(N node, SceneRequester sceneRequester) {
        super.bind(node, sceneRequester);
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
