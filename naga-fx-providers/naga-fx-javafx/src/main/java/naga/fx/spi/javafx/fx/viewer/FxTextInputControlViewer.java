package naga.fx.spi.javafx.fx.viewer;

import naga.fx.scene.control.TextInputControl;
import naga.fx.spi.viewer.base.TextInputControlViewerBase;
import naga.fx.spi.viewer.base.TextInputControlViewerMixin;

/**
 * @author Bruno Salmon
 */
abstract class FxTextInputControlViewer
        <FxN extends javafx.scene.control.TextInputControl, N extends TextInputControl, NB extends TextInputControlViewerBase<N, NB, NM>, NM extends TextInputControlViewerMixin<N, NB, NM>>
        extends FxControlViewer<FxN, N, NB, NM> {

    FxTextInputControlViewer(NB base) {
        super(base);
    }
}
