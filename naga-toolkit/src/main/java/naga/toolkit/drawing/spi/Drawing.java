package naga.toolkit.drawing.spi;

import javafx.beans.property.Property;
import naga.toolkit.drawing.shapes.Node;
import naga.toolkit.drawing.spi.view.NodeViewFactory;

/**
 * @author Bruno Salmon
 */
public interface Drawing {

    void setNodeViewFactory(NodeViewFactory nodeViewFactory);

    Property<Node> rootNodeProperty();
    default void setRootNode(Node rootNode) {
        rootNodeProperty().setValue(rootNode);
    }
    default Node getRootNode() {
        return rootNodeProperty().getValue();
    }

}
