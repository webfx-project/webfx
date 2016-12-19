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
        <FxN extends javafx.scene.chart.Chart, N extends Chart, NV extends ChartViewerBase<FxN, N, NV, NM>, NM extends ChartViewerMixin<FxN, N, NV, NM>>
        extends FxRegionViewer<FxN, N, NV, NM>
        implements ChartViewerMixin<FxN, N, NV, NM>, FxLayoutMeasurable {

    FxChartViewer(NV base) {
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
