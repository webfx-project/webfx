package naga.providers.toolkit.javafx.fx.viewer;

import naga.toolkit.fx.scene.Node;
import naga.toolkit.fx.scene.control.ButtonBase;
import naga.toolkit.fx.spi.viewer.base.ButtonBaseViewerBase;
import naga.toolkit.fx.spi.viewer.base.ButtonBaseViewerMixin;

/**
 * @author Bruno Salmon
 */
abstract class FxButtonBaseViewer
        <FxN extends javafx.scene.control.ButtonBase, N extends ButtonBase, NB extends ButtonBaseViewerBase<N, NB, NM>, NM extends ButtonBaseViewerMixin<N, NB, NM>>
        extends FxRegionViewer<FxN, N, NB, NM>
        implements ButtonBaseViewerMixin<N, NB, NM> {

    FxButtonBaseViewer(NB base) {
        super(base);
    }

    @Override
    public void updateText(String text) {
        getFxNode().setText(text);
    }

    @Override
    public void updateGraphic(Node graphic) {
        getFxNode().setGraphic(toFxNode(graphic));
    }
}
