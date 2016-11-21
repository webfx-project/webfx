package naga.providers.toolkit.javafx.drawing.view;

import javafx.scene.Node;
import naga.toolkit.drawing.shapes.EmbedGuiNode;
import naga.toolkit.drawing.spi.DrawingRequester;
import naga.toolkit.drawing.spi.view.EmbedGuiNodeView;

/**
 * @author Bruno Salmon
 */
public class FxEmbedGuiNodeView extends FxNodeViewImpl<EmbedGuiNode, Node> implements EmbedGuiNodeView {

    @Override
    public void bind(EmbedGuiNode node, DrawingRequester drawingRequester) {
        Node fxNode = node.getGuiNode().unwrapToNativeNode();
        setAndBindNodeProperties(node, fxNode);
    }

}
