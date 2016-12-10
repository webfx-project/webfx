package naga.providers.toolkit.javafx.fx.viewer;

import naga.toolkit.fx.scene.control.ButtonBase;
import naga.toolkit.fx.spi.viewer.base.ButtonBaseViewerBase;
import naga.toolkit.fx.spi.viewer.base.ButtonBaseViewerMixin;

/**
 * @author Bruno Salmon
 */
abstract class FxButtonBaseViewer
        <FxN extends javafx.scene.control.ButtonBase, N extends ButtonBase, NV extends ButtonBaseViewerBase<N, NV, NM>, NM extends ButtonBaseViewerMixin<N, NV, NM>>
        extends FxRegionViewer<FxN, N, NV, NM>
        implements ButtonBaseViewerMixin<N, NV, NM> {

    FxButtonBaseViewer(NV base) {
        super(base);
    }

    @Override
    public void updateText(String text) {
        getFxNode().setText(text);
    }
}
