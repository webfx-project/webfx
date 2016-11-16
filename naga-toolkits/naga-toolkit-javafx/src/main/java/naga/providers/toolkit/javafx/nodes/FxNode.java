package naga.providers.toolkit.javafx.nodes;

import javafx.scene.Node;
import naga.toolkit.spi.nodes.GuiNode;

/**
 * @author Bruno Salmon
 */
public class FxNode<N extends Node> implements GuiNode {

    protected final N node;

    public FxNode(N node) {
        this.node = node;
    }

    @Override
    public N unwrapToNativeNode() {
        return node;
    }

    @Override
    public void requestFocus() {
        node.requestFocus();
    }
}
