package naga.core.spi.gui.swing;

import naga.core.spi.gui.GuiNode;

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
    public N unwrapToToolkitNode() {
        return node;
    }

    @Override
    public void requestFocus() {
        node.requestFocus();
    }

}
