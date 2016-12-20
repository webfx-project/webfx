package naga.providers.toolkit.javafx.fx.viewer;

import javafx.scene.chart.NumberAxis;
import naga.toolkit.display.DisplaySelection;
import naga.toolkit.fx.ext.chart.Chart;
import naga.toolkit.fx.scene.SceneRequester;
import naga.toolkit.fx.spi.viewer.base.ChartViewerBase;
import naga.toolkit.fx.spi.viewer.base.ChartViewerMixin;
import naga.toolkit.properties.markers.SelectionMode;

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
