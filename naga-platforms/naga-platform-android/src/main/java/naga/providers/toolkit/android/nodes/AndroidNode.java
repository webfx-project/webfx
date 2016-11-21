package naga.providers.toolkit.android.nodes;

import android.view.View;
import naga.toolkit.spi.nodes.GuiNode;

/**
 * @author Bruno Salmon
 */
public class AndroidNode<N extends View> implements GuiNode {

    protected final N node;

    public AndroidNode(N node) {
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