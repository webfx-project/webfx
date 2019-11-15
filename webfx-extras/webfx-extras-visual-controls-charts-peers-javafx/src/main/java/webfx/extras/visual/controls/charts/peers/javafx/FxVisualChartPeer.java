package webfx.extras.visual.controls.charts.peers.javafx;

import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.Chart;
import javafx.scene.chart.NumberAxis;
import webfx.extras.visual.SelectionMode;
import webfx.extras.visual.VisualSelection;
import webfx.extras.visual.controls.charts.VisualChart;
import webfx.extras.visual.controls.charts.peers.base.VisualChartPeerBase;
import webfx.extras.visual.controls.charts.peers.base.VisualChartPeerMixin;
import webfx.kit.mapper.peers.javafxgraphics.SceneRequester;
import webfx.kit.mapper.peers.javafxgraphics.javafx.FxLayoutMeasurable;
import webfx.kit.mapper.peers.javafxgraphics.javafx.FxRegionPeer;

/**
 * @author Bruno Salmon
 */
abstract class FxVisualChartPeer
        <FxN extends Chart, N extends VisualChart, NB extends VisualChartPeerBase<FxN, N, NB, NM>, NM extends VisualChartPeerMixin<FxN, N, NB, NM>>
        extends FxRegionPeer<FxN, N, NB, NM>
        implements VisualChartPeerMixin<FxN, N, NB, NM>, FxLayoutMeasurable {

    FxVisualChartPeer(NB base) {
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
    public void updateVisualSelection(VisualSelection selection) {
    }

    static NumberAxis createNumberAxis() {
        NumberAxis axis = new NumberAxis();
        axis.setForceZeroInRange(false);
        return axis;
    }

    static CategoryAxis createCategoryAxis() {
        CategoryAxis axis = new CategoryAxis();
        return axis;
    }
}
