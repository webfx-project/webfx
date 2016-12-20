package naga.toolkit.fx.spi.viewer.base;

import naga.toolkit.fx.scene.control.Button;

/**
 * @author Bruno Salmon
 */
public interface ButtonViewerMixin
        <N extends Button, NV extends ButtonViewerBase<N, NV, NM>, NM extends ButtonViewerMixin<N, NV, NM>>

        extends ButtonBaseViewerMixin<N, NV, NM> {
}
