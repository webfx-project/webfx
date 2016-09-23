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

    private final Node[] customContainers;

    public HtmlVPage() {
        this(createDiv());
    }

    private static HTMLDivElement createDiv() {
        return setStyle(createElement("div"), "width: 100%");
    }

    public HtmlVPage(HTMLDivElement div) {
        this(div, null, null, null);
    }

    public HtmlVPage(HTMLDivElement node, Node customHeaderContainer, Node customCenterContainer, Node customFooterContainer) {
        super(node);
        customContainers = customCenterContainer == null ? null : new Node[]{customHeaderContainer, customCenterContainer, customFooterContainer};
        ChangeListener<GuiNode<Node>> onAnyNodePropertyChange = (observable, oldValue, newValue) -> populate();
        for (int i = 0; i < 3; i++) {
            childrenProperties[i] = new SimpleObjectProperty<>();
            childrenProperties[i].addListener(onAnyNodePropertyChange);
        }
    }

    private void populate() {
        if (customContainers == null)
            removeChildren();
        for (int i = 0; i < 3; i++) {
            Property<GuiNode<Node>> childProperty = childrenProperties[i];
            GuiNode<Node> childPropertyValue = childProperty.getValue();
            Node child = childPropertyValue == null ? null : prepareChild(childPropertyValue.unwrapToNativeNode());
            if (customContainers == null)
                appendChild(node, child);
            else
                setChild(customContainers[i], child);
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
