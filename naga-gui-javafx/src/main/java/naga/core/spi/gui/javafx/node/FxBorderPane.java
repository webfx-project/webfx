package naga.core.spi.gui.javafx.node;

import javafx.beans.property.Property;
import javafx.scene.layout.BorderPane;
import naga.core.ngui.property.NodeProperty;
import naga.core.spi.gui.node.Node;

/**
 * @author Bruno Salmon
 */
public class FxBorderPane extends FxNode<BorderPane> implements naga.core.spi.gui.node.BorderPane<BorderPane, javafx.scene.Node> {

    public FxBorderPane() {
        this(new BorderPane());
    }

    public FxBorderPane(BorderPane borderPane) {
        super(borderPane);
    }

    private NodeProperty topProperty;
    @Override
    public Property<Node<javafx.scene.Node>> topProperty() {
        if (topProperty == null) topProperty = new NodeProperty(node.topProperty());
        return topProperty;
    }

    private NodeProperty centerProperty;
    @Override
    public Property<Node<javafx.scene.Node>> centerProperty() {
        if (centerProperty == null) centerProperty = new NodeProperty(node.centerProperty());
        return centerProperty;
    }

    private NodeProperty bottomProperty;
    @Override
    public Property<Node<javafx.scene.Node>> bottomProperty() {
        if (bottomProperty == null) bottomProperty = new NodeProperty(node.bottomProperty());
        return bottomProperty;
    }
}
