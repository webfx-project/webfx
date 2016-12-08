package naga.providers.toolkit.javafx.fx.view;

import naga.toolkit.fx.scene.control.ButtonBase;
import naga.toolkit.fx.spi.view.base.ButtonBaseViewBase;
import naga.toolkit.fx.spi.view.base.ButtonBaseViewMixin;

/**
 * @author Bruno Salmon
 */
abstract class FxButtonBaseView
        <FxN extends javafx.scene.control.ButtonBase, N extends ButtonBase, NV extends ButtonBaseViewBase<N, NV, NM>, NM extends ButtonBaseViewMixin<N, NV, NM>>
        extends FxRegionView<FxN, N, NV, NM>
        implements ButtonBaseViewMixin<N, NV, NM> {

    FxButtonBaseView(NV base) {
        super(base);
    }

    @Override
    public void updateText(String text) {
        getFxNode().setText(text);
    }
}
