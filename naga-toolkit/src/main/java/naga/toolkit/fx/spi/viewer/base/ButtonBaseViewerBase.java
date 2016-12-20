package naga.toolkit.fx.spi.viewer.base;

import naga.toolkit.fx.scene.control.ButtonBase;

/**
 * @author Bruno Salmon
 */
public abstract class ButtonBaseViewerBase
        <N extends ButtonBase, NB extends ButtonBaseViewerBase<N, NB, NM>, NM extends ButtonBaseViewerMixin<N, NB, NM>>

        extends LabeledViewerBase<N, NB, NM> {
}
