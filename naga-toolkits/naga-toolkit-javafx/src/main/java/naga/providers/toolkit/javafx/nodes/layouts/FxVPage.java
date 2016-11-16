package naga.providers.toolkit.javafx.nodes.layouts;

import javafx.beans.property.Property;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import naga.providers.toolkit.javafx.nodes.FxNode;
import naga.toolkit.properties.conversion.NodeProperty;
import naga.toolkit.spi.nodes.GuiNode;
import naga.toolkit.spi.nodes.layouts.VPage;

/**
 * @author Bruno Salmon
 */
public class FxVPage extends FxNode<BorderPane> implements VPage {

    public FxVPage() {
        this(createBorderPane());
    }

    public FxVPage(BorderPane borderPane) {
        super(borderPane);
    }

    private static BorderPane createBorderPane() {
        BorderPane borderPane = new BorderPane();
        borderPane.setBackground(Background.EMPTY);
        return borderPane;
    }

    private NodeProperty<Node> headerProperty;
    @Override
    public Property<GuiNode> headerProperty() {
        if (headerProperty == null) headerProperty = new NodeProperty<>(node.topProperty());
        return headerProperty;
    }

    private NodeProperty<Node> centerProperty;
    @Override
    public Property<GuiNode> centerProperty() {
        if (centerProperty == null) centerProperty = new NodeProperty<>(node.centerProperty(), FxVPage::embedInScrollPaneWhenNecessary);
        return centerProperty;
    }

    private static Node embedInScrollPaneWhenNecessary(Node node) {
        if (node == null || node instanceof TableView || node instanceof BorderPane)
            return node;
        Object nodeScrollPane = node.getProperties().get("scrollPane");
        if (nodeScrollPane instanceof ScrollPane)
            return (Node) nodeScrollPane;
        ScrollPane scrollPane = new ScrollPane(node);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        node.getProperties().put("scrollPane", scrollPane);
        return scrollPane;
    }

    private NodeProperty<Node> footerProperty;
    @Override
    public Property<GuiNode> footerProperty() {
        if (footerProperty == null) footerProperty = new NodeProperty<>(node.bottomProperty());
        return footerProperty;
    }
}
