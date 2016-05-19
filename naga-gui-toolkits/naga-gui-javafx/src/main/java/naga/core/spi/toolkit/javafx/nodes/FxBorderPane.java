package naga.core.spi.toolkit.javafx.nodes;

import javafx.beans.property.Property;
import javafx.scene.Node;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import naga.core.spi.toolkit.property.NodeProperty;
import naga.core.spi.toolkit.GuiNode;
import naga.core.spi.toolkit.javafx.FxNode;

/**
 * @author Bruno Salmon
 */
public class FxBorderPane extends FxNode<BorderPane> implements naga.core.spi.toolkit.nodes.BorderPane<BorderPane, javafx.scene.Node> {

    public FxBorderPane() {
        this(new BorderPane());
    }

    public FxBorderPane(BorderPane borderPane) {
        super(borderPane);
        borderPane.setBackground(Background.EMPTY);
    }

    private NodeProperty topProperty;
    @Override
    public Property<GuiNode<Node>> topProperty() {
        if (topProperty == null) topProperty = new NodeProperty(node.topProperty());
        return topProperty;
    }

    private NodeProperty centerProperty;
    @Override
    public Property<GuiNode<Node>> centerProperty() {
        if (centerProperty == null) centerProperty = new NodeProperty(node.centerProperty());
        return centerProperty;
    }

    private NodeProperty bottomProperty;
    @Override
    public Property<GuiNode<Node>> bottomProperty() {
        if (bottomProperty == null) bottomProperty = new NodeProperty(node.bottomProperty());
        return bottomProperty;
    }
}
