package naga.core.spi.gui.pivot;

import naga.core.spi.gui.GuiNode;
import org.apache.pivot.wtk.Component;

/**
 * @author Bruno Salmon
 */
public class PivotNode<N extends Component> implements GuiNode<N> {

    protected final N node;

    public PivotNode(N node) {
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
