package naga.providers.toolkit.html.nodes.layouts;

import elemental2.HTMLDivElement;
import elemental2.Node;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import naga.providers.toolkit.html.nodes.HtmlNode;
import naga.toolkit.spi.nodes.GuiNode;
import naga.toolkit.spi.nodes.layouts.VPage;

import static naga.providers.toolkit.html.HtmlUtil.*;

/**
 * @author Bruno Salmon
 */
public class HtmlVPage extends HtmlNode<HTMLDivElement> implements VPage<HTMLDivElement, Node> {

    public HtmlVPage() {
        this(createDiv());
    }

    public HtmlVPage(HTMLDivElement node) {
        super(node);
        ChangeListener<GuiNode<Node>> onAnyNodePropertyChange = (observable, oldValue, newValue) -> populate();
        for (int i = 0; i < childrenProperties.length; i++) {
            childrenProperties[i] = new SimpleObjectProperty<>();
            childrenProperties[i].addListener(onAnyNodePropertyChange);
        }
    }

    private static HTMLDivElement createDiv() {
        return setStyle(createElement("div"), "width: 100%");
    }

    private void populate() {
        removeChildren();
        for (Property<GuiNode<Node>> childProperty : childrenProperties) {
            GuiNode<Node> childPropertyValue = childProperty.getValue();
            if (childPropertyValue != null)
                node.appendChild(prepareChild(childPropertyValue.unwrapToNativeNode()));
        }
    }

    protected Node prepareChild(Node child) {
        return appendChild(createDiv(), appendStyle(child, "width: 100%"));
    }


    private final Property<GuiNode<Node>>[] childrenProperties = new Property[3];

    @Override
    public Property<GuiNode<Node>> headerProperty() {
        return childrenProperties[0];
    }

    @Override
    public Property<GuiNode<Node>> centerProperty() {
        return childrenProperties[1];
    }

    @Override
    public Property<GuiNode<Node>> footerProperty() {
        return childrenProperties[2];
    }
}
