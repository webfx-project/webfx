package naga.providers.toolkit.javafx.fx.view;

import naga.toolkit.fx.scene.control.Control;
import naga.toolkit.fx.spi.view.ControlView;
import naga.toolkit.fx.spi.view.base.ControlViewBase;
import naga.toolkit.fx.spi.view.base.ControlViewMixin;

/**
 * @author Bruno Salmon
 */
abstract class FxControlView
        <FxN extends javafx.scene.control.Control, N extends Control, NV extends ControlViewBase<N, NV, NM>, NM extends ControlViewMixin<N, NV, NM>>
        extends FxRegionView<FxN, N, NV, NM>
        implements ControlView<N> {

    FxControlView(NV base) {
        super(base);
    }
}
