package naga.providers.toolkit.javafx.fx.view;

import javafx.scene.Node;
import naga.toolkit.fx.scene.EmbedGuiNode;
import naga.toolkit.fx.spi.view.base.EmbedGuiNodeViewBase;
import naga.toolkit.fx.spi.view.base.EmbedGuiNodeViewMixin;

/**
 * @author Bruno Salmon
 */
public class FxEmbedGuiNodeView

        extends FxNodeView<Node, EmbedGuiNode, EmbedGuiNodeViewBase, EmbedGuiNodeViewMixin>
        implements EmbedGuiNodeViewMixin {

    public FxEmbedGuiNodeView() {
        super(new EmbedGuiNodeViewBase());
    }

    @Override
    Node createFxNode() {
        return getNode().getGuiNode().unwrapToNativeNode();
    }
}
