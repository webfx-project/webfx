package naga.providers.toolkit.swing.fx.view;

import naga.toolkit.fx.scene.EmbedGuiNode;
import naga.toolkit.fx.spi.view.base.EmbedGuiNodeViewBase;
import naga.toolkit.fx.spi.view.base.EmbedGuiNodeViewMixin;

import javax.swing.*;

/**
 * @author Bruno Salmon
 */
public class SwingEmbedGuiNodeView extends SwingNodeView<EmbedGuiNode, EmbedGuiNodeViewBase, EmbedGuiNodeViewMixin>
    implements SwingEmbedComponentView<EmbedGuiNode> {

    public SwingEmbedGuiNodeView() {
        super(new EmbedGuiNodeViewBase());
    }

    public JComponent getSwingComponent() {
        return getNode().getGuiNode().unwrapToNativeNode();
    }
}
