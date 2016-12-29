package naga.fx.spi.swing.fx.viewer;

import naga.fx.scene.control.Control;
import naga.fx.spi.viewer.base.ControlViewerBase;
import naga.fx.spi.viewer.base.ControlViewerMixin;

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
