package naga.providers.toolkit.html.nodes;

import elemental2.Element;
import elemental2.Node;
import naga.providers.toolkit.html.HtmlUtil;
import naga.toolkit.spi.nodes.GuiNode;

/**
 * @author Bruno Salmon
 */
public class HtmlNode<N extends Node> implements GuiNode<N> {

    protected final N node;

    public HtmlNode(N node) {
        this.node = node;
    }

    @Override
    public N unwrapToNativeNode() {
        return node;
    }

    @Override
    public void requestFocus() {
        if (node instanceof Element)
            ((Element) node).focus();
    }

    public void removeChildren() {
        HtmlUtil.removeChildren(node);
    }
}
