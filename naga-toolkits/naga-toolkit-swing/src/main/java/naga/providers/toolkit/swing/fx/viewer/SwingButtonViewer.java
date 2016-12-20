package naga.providers.toolkit.swing.fx.viewer;

import naga.toolkit.fx.scene.control.Button;
import naga.toolkit.fx.spi.viewer.base.ButtonViewerBase;
import naga.toolkit.fx.spi.viewer.base.ButtonViewerMixin;

import javax.swing.*;

/**
 * @author Bruno Salmon
 */
public class SwingButtonViewer
        <N extends Button, NV extends ButtonViewerBase<N, NV, NM>, NM extends ButtonViewerMixin<N, NV, NM>>

        extends SwingButtonBaseViewer<N, NV, NM>
        implements ButtonViewerMixin<N, NV, NM> {

    public SwingButtonViewer() {
        this((NV) new ButtonViewerBase(), new JButton());
    }

    public SwingButtonViewer(NV base, AbstractButton swingButtonBase) {
        super(base, swingButtonBase);
    }
}
