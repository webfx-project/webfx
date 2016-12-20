package naga.toolkit.fx.spi.viewer.base;

import naga.toolkit.fx.scene.control.Button;

/**
 * @author Bruno Salmon
 */
public class ButtonViewerBase
        <N extends Button, NV extends ButtonViewerBase<N, NV, NM>, NM extends ButtonViewerMixin<N, NV, NM>>

        extends ButtonBaseViewerBase<N, NV, NM> {
}
