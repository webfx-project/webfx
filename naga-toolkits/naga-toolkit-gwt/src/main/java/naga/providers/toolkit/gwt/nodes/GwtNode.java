package naga.providers.toolkit.gwt.nodes;

import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.UIObject;
import naga.toolkit.spi.nodes.GuiNode;

/**
 * @author Bruno Salmon
 */
public class GwtNode<N extends UIObject> implements GuiNode {

    protected final N node;

    public GwtNode(N node) {
        this.node = node;
    }

    @Override
    public N unwrapToNativeNode() {
        return node;
    }

    @Override
    public void requestFocus() {
        if (node instanceof Focusable)
            ((Focusable) node).setFocus(true);
    }
}
