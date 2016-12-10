package naga.toolkit.fx.spi.viewer.base;

import naga.toolkit.fx.scene.control.ButtonBase;
import naga.toolkit.fx.spi.viewer.ButtonBaseViewer;

/**
 * @author Bruno Salmon
 */
public interface ButtonBaseViewerMixin
        <N extends ButtonBase, NV extends ButtonBaseViewerBase<N, NV, NM>, NM extends ButtonBaseViewerMixin<N, NV, NM>>

        extends ButtonBaseViewer<N>,
        LabeledViewerMixin<N, NV, NM> {
}
