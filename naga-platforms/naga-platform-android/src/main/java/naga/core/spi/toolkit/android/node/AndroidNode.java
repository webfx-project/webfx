package naga.core.spi.toolkit.android.node;

import android.view.View;
import naga.core.spi.toolkit.node.GuiNode;

/**
 * @author Bruno Salmon
 */
public class AndroidNode<N extends View> implements GuiNode<N> {

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