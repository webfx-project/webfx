package naga.toolkit.fx.spi.viewer.base;

import naga.toolkit.fx.scene.control.ButtonBase;

/**
 * @author Bruno Salmon
 */
public abstract class ButtonBaseViewerBase
        <N extends ButtonBase, NV extends ButtonBaseViewerBase<N, NV, NM>, NM extends ButtonBaseViewerMixin<N, NV, NM>>

        extends LabeledViewerBase<N, NV, NM> {

}
