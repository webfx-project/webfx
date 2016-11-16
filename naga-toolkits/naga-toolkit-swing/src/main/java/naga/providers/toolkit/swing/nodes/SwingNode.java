package naga.providers.toolkit.swing.nodes;

import naga.toolkit.spi.nodes.GuiNode;

import java.awt.*;

/**
 * @author Bruno Salmon
 */
public class SwingNode<N extends Component> implements GuiNode {

    protected final N node;

    public SwingNode(N node) {
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
