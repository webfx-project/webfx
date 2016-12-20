package naga.providers.toolkit.swing.fx.viewer;

import naga.toolkit.fx.scene.control.Button;
import naga.toolkit.fx.spi.viewer.base.ButtonViewerBase;
import naga.toolkit.fx.spi.viewer.base.ButtonViewerMixin;

import javax.swing.*;

/**
 * @author Bruno Salmon
 */
public class SwingButtonViewer
        <N extends Button, NB extends ButtonViewerBase<N, NB, NM>, NM extends ButtonViewerMixin<N, NB, NM>>

        extends SwingButtonBaseViewer<N, NB, NM>
        implements ButtonViewerMixin<N, NB, NM> {

    public SwingButtonViewer() {
        this((NB) new ButtonViewerBase(), new JButton());
    }

    public SwingButtonViewer(NB base, AbstractButton swingButtonBase) {
        super(base, swingButtonBase);
    }
}
