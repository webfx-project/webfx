package naga.providers.toolkit.javafx.drawing.view;

import javafx.scene.Node;
import naga.toolkit.drawing.shapes.EmbedGuiNode;
import naga.toolkit.drawing.spi.view.EmbedGuiNodeView;

/**
 * @author Bruno Salmon
 */
public class FxEmbedGuiNodeView extends FxNodeViewImpl<EmbedGuiNode, Node> implements EmbedGuiNodeView {

    @Override
    Node createFxNode(EmbedGuiNode node) {
        return node.getGuiNode().unwrapToNativeNode();
    }
}
