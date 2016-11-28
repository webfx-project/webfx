package naga.toolkit.drawing.spi.view.base;

import naga.toolkit.drawing.scene.control.ButtonBase;
import naga.toolkit.drawing.spi.view.ButtonBaseView;

/**
 * @author Bruno Salmon
 */
public interface ButtonBaseViewMixin
        <N extends ButtonBase, NV extends ButtonBaseViewBase<N, NV, NM>, NM extends ButtonBaseViewMixin<N, NV, NM>>

        extends ButtonBaseView<N>,
        LabeledViewMixin<N, NV, NM> {
}
