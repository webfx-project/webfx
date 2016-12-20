package naga.providers.toolkit.javafx.fx.viewer;

import naga.toolkit.fx.scene.control.Control;
import naga.toolkit.fx.spi.viewer.base.ControlViewerBase;
import naga.toolkit.fx.spi.viewer.base.ControlViewerMixin;

/**
 * @author Bruno Salmon
 */
abstract class FxControlViewer
        <FxN extends javafx.scene.control.Control, N extends Control, NV extends ControlViewerBase<N, NV, NM>, NM extends ControlViewerMixin<N, NV, NM>>
        extends FxRegionViewer<FxN, N, NV, NM> {

    FxControlViewer(NV base) {
        super(base);
    }
}
