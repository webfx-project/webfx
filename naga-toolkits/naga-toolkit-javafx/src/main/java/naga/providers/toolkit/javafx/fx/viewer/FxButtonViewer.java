package naga.providers.toolkit.javafx.fx.viewer;

import naga.toolkit.fx.scene.control.Button;
import naga.toolkit.fx.spi.viewer.base.ButtonViewerBase;
import naga.toolkit.fx.spi.viewer.base.ButtonViewerMixin;

/**
 * @author Bruno Salmon
 */
public class FxButtonViewer
        <FxN extends javafx.scene.control.Button, N extends Button, NV extends ButtonViewerBase<N, NV, NM>, NM extends ButtonViewerMixin<N, NV, NM>>

        extends FxButtonBaseViewer<FxN, N, NV, NM>
        implements ButtonViewerMixin<N, NV, NM>, FxLayoutMeasurable {

    public FxButtonViewer() {
        super((NV) new ButtonViewerBase());
    }

    @Override
    protected FxN createFxNode() {
        return (FxN) new javafx.scene.control.Button();
    }
}
