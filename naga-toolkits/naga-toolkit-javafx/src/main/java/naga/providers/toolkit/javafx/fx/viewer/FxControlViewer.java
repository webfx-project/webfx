package naga.providers.toolkit.javafx.fx.viewer;

import naga.toolkit.fx.scene.control.Control;
import naga.toolkit.fx.spi.viewer.base.ControlViewerBase;
import naga.toolkit.fx.spi.viewer.base.ControlViewerMixin;

/**
 * @author Bruno Salmon
 */
abstract class FxControlViewer
        <FxN extends javafx.scene.control.Control, N extends Control, NB extends ControlViewerBase<N, NB, NM>, NM extends ControlViewerMixin<N, NB, NM>>
        extends FxRegionViewer<FxN, N, NB, NM> {

    FxControlViewer(NB base) {
        super(base);
    }
}
