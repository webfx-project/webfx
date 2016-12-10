package naga.providers.toolkit.swing.fx.viewer;

import naga.toolkit.fx.scene.control.Button;
import naga.toolkit.fx.spi.viewer.base.ButtonViewerBase;
import naga.toolkit.fx.spi.viewer.base.ButtonViewerMixin;

import javax.swing.*;

/**
 * @author Bruno Salmon
 */
public class SwingButtonViewer
        extends SwingButtonBaseViewer<Button, ButtonViewerBase, ButtonViewerMixin>
        implements ButtonViewerMixin {

    public SwingButtonViewer() {
        super(new ButtonViewerBase(), new JButton());
    }
}
