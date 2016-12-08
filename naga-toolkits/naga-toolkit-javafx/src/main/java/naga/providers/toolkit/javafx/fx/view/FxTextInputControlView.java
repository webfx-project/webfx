package naga.providers.toolkit.javafx.fx.view;

import naga.toolkit.fx.scene.control.TextInputControl;
import naga.toolkit.fx.spi.view.ControlView;
import naga.toolkit.fx.spi.view.base.TextInputControlViewBase;
import naga.toolkit.fx.spi.view.base.TextInputControlViewMixin;

/**
 * @author Bruno Salmon
 */
abstract class FxTextInputControlView
        <FxN extends javafx.scene.control.TextInputControl, N extends TextInputControl, NV extends TextInputControlViewBase<N, NV, NM>, NM extends TextInputControlViewMixin<N, NV, NM>>
        extends FxControlView<FxN, N, NV, NM>
        implements ControlView<N> {

    FxTextInputControlView(NV base) {
        super(base);
    }
}
