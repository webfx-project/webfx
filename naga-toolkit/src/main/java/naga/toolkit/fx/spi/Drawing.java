package naga.toolkit.fx.spi;

import javafx.beans.property.Property;
import naga.toolkit.fx.scene.Node;
import naga.toolkit.fx.spi.view.NodeViewFactory;

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
