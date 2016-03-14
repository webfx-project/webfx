package naga.core.spi.gui.javafx.node;

import javafx.scene.Node;

/**
 * @author Bruno Salmon
 */
public class FxNode<N extends Node> implements naga.core.spi.gui.node.Node<N> {

    protected final N node;

    public FxNode(N node) {
        this.node = node;
    }

    @Override
    public N unwrapToToolkitNode() {
        return node;
    }

    @Override
    public void requestFocus() {
        node.requestFocus();
    }
}
