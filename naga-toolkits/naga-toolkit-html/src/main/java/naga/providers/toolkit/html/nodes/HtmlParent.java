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
public class HtmlParent<P extends Element> extends HtmlNode<P> implements Parent<P, Element> {

    public HtmlParent(P node) {
        super(node);
        children.addListener(this::onChanged);
    }

    private final ObservableList<GuiNode<Element>> children = FXCollections.observableArrayList();
    @Override
    public ObservableList<GuiNode<Element>> getChildren() {
        return children;
    }

    private void onChanged(ListChangeListener.Change<? extends GuiNode<Element>> change) {
        removeChildren();
        for (GuiNode<Element> child : children)
            node.appendChild(prepareChild(child.unwrapToNativeNode()));
    }

    protected Element prepareChild(Element child) {
        return child;
    }

}
