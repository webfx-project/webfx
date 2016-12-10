package naga.providers.toolkit.swing.fx.viewer;

import naga.toolkit.fx.scene.EmbedGuiNode;
import naga.toolkit.fx.spi.viewer.base.EmbedGuiNodeViewerBase;
import naga.toolkit.fx.spi.viewer.base.EmbedGuiNodeViewerMixin;

import javax.swing.*;

/**
 * @author Bruno Salmon
 */
public class SwingEmbedGuiNodeViewer extends SwingNodeViewer<EmbedGuiNode, EmbedGuiNodeViewerBase, EmbedGuiNodeViewerMixin>
    implements SwingEmbedComponentViewer<EmbedGuiNode> {

    public SwingEmbedGuiNodeViewer() {
        super(new EmbedGuiNodeViewerBase());
    }

    public JComponent getSwingComponent() {
        return getNode().getGuiNode().unwrapToNativeNode();
    }
}
