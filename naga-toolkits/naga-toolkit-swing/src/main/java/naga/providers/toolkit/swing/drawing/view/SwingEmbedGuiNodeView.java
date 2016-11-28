package naga.providers.toolkit.swing.drawing.view;

import naga.toolkit.drawing.scene.EmbedGuiNode;
import naga.toolkit.drawing.spi.view.base.EmbedGuiNodeViewBase;
import naga.toolkit.drawing.spi.view.base.EmbedGuiNodeViewMixin;

import javax.swing.*;

/**
 * @author Bruno Salmon
 */
public class SwingEmbedGuiNodeView extends SwingNodeView<EmbedGuiNode, EmbedGuiNodeViewBase, EmbedGuiNodeViewMixin>
    implements SwingEmbedComponentView<EmbedGuiNode> {

    public SwingEmbedGuiNodeView() {
        super(new EmbedGuiNodeViewBase());
    }

    public JComponent getEmbedSwingComponent() {
        return getNode().getGuiNode().unwrapToNativeNode();
    }
}
