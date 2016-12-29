package naga.fx.spi.viewer.base;

import naga.fx.scene.control.ButtonBase;

/**
 * @author Bruno Salmon
 */
public interface ButtonBaseViewerMixin
        <N extends ButtonBase, NB extends ButtonBaseViewerBase<N, NB, NM>, NM extends ButtonBaseViewerMixin<N, NB, NM>>

        extends LabeledViewerMixin<N, NB, NM> {
}
