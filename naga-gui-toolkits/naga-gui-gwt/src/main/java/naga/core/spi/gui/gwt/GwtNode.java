package naga.core.spi.gui.gwt;

import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.UIObject;
import naga.core.spi.gui.GuiNode;

/**
 * @author Bruno Salmon
 */
public class GwtNode <N extends UIObject> implements GuiNode<N> {

    protected final N node;

    public GwtNode(N node) {
        this.node = node;
    }

    @Override
    public N unwrapToToolkitNode() {
        return node;
    }

    @Override
    public void requestFocus() {
        if (node instanceof Focusable)
            ((Focusable) node).setFocus(true);
    }
}
