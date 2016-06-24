package naga.core.spi.toolkit.javafx.layouts;

import javafx.beans.property.Property;
import javafx.scene.Node;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import naga.core.spi.toolkit.layouts.VPage;
import naga.core.spi.toolkit.property.NodeProperty;
import naga.core.spi.toolkit.node.GuiNode;
import naga.core.spi.toolkit.javafx.node.FxNode;

/**
 * @author Bruno Salmon
 */
public class FxVPage extends FxNode<BorderPane> implements VPage<BorderPane, Node> {

    public FxVPage() {
        this(new BorderPane());
    }

    public FxVPage(BorderPane borderPane) {
        super(borderPane);
        borderPane.setBackground(Background.EMPTY);
    }

    private NodeProperty<Node> headerProperty;
    @Override
    public Property<GuiNode<Node>> headerProperty() {
        if (headerProperty == null) headerProperty = new NodeProperty<>(node.topProperty());
        return headerProperty;
    }

    private NodeProperty<Node> centerProperty;
    @Override
    public Property<GuiNode<Node>> centerProperty() {
        if (centerProperty == null) centerProperty = new NodeProperty<>(node.centerProperty());
        return centerProperty;
    }

    private NodeProperty<Node> footerProperty;
    @Override
    public Property<GuiNode<Node>> footerProperty() {
        if (footerProperty == null) footerProperty = new NodeProperty<>(node.bottomProperty());
        return footerProperty;
    }
}
