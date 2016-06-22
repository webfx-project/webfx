package naga.core.spi.toolkit.javafx;

import javafx.scene.Node;
import naga.core.spi.toolkit.GuiNode;

/**
 * @author Bruno Salmon
 */
public class FxNode<N extends Node> implements GuiNode<N> {

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
