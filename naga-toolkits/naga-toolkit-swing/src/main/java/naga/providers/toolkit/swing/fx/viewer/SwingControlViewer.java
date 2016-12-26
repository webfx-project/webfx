package naga.providers.toolkit.swing.fx.viewer;

import naga.toolkit.fx.scene.control.Control;
import naga.toolkit.fx.spi.viewer.base.ControlViewerBase;
import naga.toolkit.fx.spi.viewer.base.ControlViewerMixin;

/**
 * @author Bruno Salmon
 */
public abstract class SwingControlViewer
        <N extends Control, NB extends ControlViewerBase<N, NB, NM>, NM extends ControlViewerMixin<N, NB, NM>>

        extends SwingRegionViewer<N, NB, NM>
        implements ControlViewerMixin<N, NB, NM> {


    SwingControlViewer(NB base) {
        super(base);
    }

}
