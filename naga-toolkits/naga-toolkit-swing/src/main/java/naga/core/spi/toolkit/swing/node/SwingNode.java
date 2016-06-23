package naga.core.spi.toolkit.swing.node;

import naga.core.spi.toolkit.node.GuiNode;

import java.awt.*;

/**
 * @author Bruno Salmon
 */
public class SwingNode<N extends Component> implements GuiNode<N> {

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
