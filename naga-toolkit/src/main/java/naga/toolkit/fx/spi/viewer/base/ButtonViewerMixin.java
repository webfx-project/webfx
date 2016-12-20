package naga.toolkit.fx.spi.viewer.base;

import naga.toolkit.fx.scene.control.Button;

/**
 * @author Bruno Salmon
 */
public interface ButtonViewerMixin
        <N extends Button, NB extends ButtonViewerBase<N, NB, NM>, NM extends ButtonViewerMixin<N, NB, NM>>

        extends ButtonBaseViewerMixin<N, NB, NM> {
}
