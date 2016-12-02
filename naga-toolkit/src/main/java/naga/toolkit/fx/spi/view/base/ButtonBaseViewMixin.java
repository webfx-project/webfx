package naga.toolkit.fx.spi.view.base;

import naga.toolkit.fx.scene.control.ButtonBase;
import naga.toolkit.fx.spi.view.ButtonBaseView;

/**
 * @author Bruno Salmon
 */
public interface ButtonBaseViewMixin
        <N extends ButtonBase, NV extends ButtonBaseViewBase<N, NV, NM>, NM extends ButtonBaseViewMixin<N, NV, NM>>

        extends ButtonBaseView<N>,
        LabeledViewMixin<N, NV, NM> {
}
