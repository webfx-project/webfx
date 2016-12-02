package naga.providers.toolkit.javafx.fx.view;

import javafx.scene.Node;
import naga.toolkit.fx.scene.EmbedGuiNode;
import naga.toolkit.fx.spi.view.EmbedGuiNodeView;

/**
 * @author Bruno Salmon
 */
public class FxEmbedGuiNodeView extends FxNodeViewImpl<EmbedGuiNode, Node> implements EmbedGuiNodeView {

    @Override
    Node createFxNode(EmbedGuiNode node) {
        return node.getGuiNode().unwrapToNativeNode();
    }
}
