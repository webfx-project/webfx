package naga.providers.toolkit.javafx.fx.viewer;

import naga.toolkit.fx.scene.control.TextInputControl;
import naga.toolkit.fx.spi.viewer.ControlViewer;
import naga.toolkit.fx.spi.viewer.base.TextInputControlViewerBase;
import naga.toolkit.fx.spi.viewer.base.TextInputControlViewerMixin;

/**
 * @author Bruno Salmon
 */
abstract class FxTextInputControlViewer
        <FxN extends javafx.scene.control.TextInputControl, N extends TextInputControl, NV extends TextInputControlViewerBase<N, NV, NM>, NM extends TextInputControlViewerMixin<N, NV, NM>>
        extends FxControlViewer<FxN, N, NV, NM>
        implements ControlViewer<N> {

    FxTextInputControlViewer(NV base) {
        super(base);
    }
}
