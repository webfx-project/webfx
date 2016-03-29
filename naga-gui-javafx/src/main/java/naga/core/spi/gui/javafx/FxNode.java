package naga.core.spi.gui.javafx;

import javafx.scene.Node;
import naga.core.spi.gui.GuiNode;

/**
 * @author Bruno Salmon
 */
public class FxNode<N extends Node> implements GuiNode<N> {

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
