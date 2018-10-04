package webfx.fxkit.javafx.mapper.peer;

import javafx.scene.chart.NumberAxis;
import webfx.fxkit.extra.displaydata.DisplaySelection;
import webfx.fxkit.extra.chart.Chart;
import webfx.fxkit.mapper.spi.SceneRequester;
import webfx.fxkit.extra.mapper.spi.peer.impl.ChartPeerMixin;
import webfx.fxkit.extra.mapper.spi.peer.impl.ChartPeerBase;
import webfx.fxkit.extra.displaydata.SelectionMode;

/**
 * @author Bruno Salmon
 */
abstract class FxChartPeer
        <FxN extends javafx.scene.chart.Chart, N extends Chart, NB extends ChartPeerBase<FxN, N, NB, NM>, NM extends ChartPeerMixin<FxN, N, NB, NM>>
        extends FxRegionPeer<FxN, N, NB, NM>
        implements ChartPeerMixin<FxN, N, NB, NM>, FxLayoutMeasurable {

    FxChartPeer(NB base) {
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
