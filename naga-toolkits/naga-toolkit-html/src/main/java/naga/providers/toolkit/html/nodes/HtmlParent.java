package naga.providers.toolkit.html.nodes;

import elemental2.Element;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import naga.toolkit.spi.nodes.GuiNode;
import naga.toolkit.spi.nodes.Parent;

/**
 * @author Bruno Salmon
 */
public class HtmlParent<P extends Element> extends HtmlNode<P> implements Parent {

    public HtmlParent(P node) {
        super(node);
        children.addListener(this::onChanged);
    }

    private final ObservableList<GuiNode> children = FXCollections.observableArrayList();
    @Override
    public ObservableList<GuiNode> getChildren() {
        return children;
    }

    private void onChanged(ListChangeListener.Change<? extends GuiNode> change) {
        removeChildren();
        for (GuiNode child : children)
            node.appendChild(prepareChild(child.unwrapToNativeNode()));
    }

    protected Element prepareChild(Element child) {
        return child;
    }

}
