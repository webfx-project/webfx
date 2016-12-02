package naga.toolkit.fx.spi;

import javafx.beans.property.Property;
import naga.toolkit.fx.scene.Node;
import naga.toolkit.fx.spi.view.NodeViewFactory;

/**
 * @author Bruno Salmon
 */
public interface DrawingMixin extends Drawing {

    Drawing getDrawing();

    default void setNodeViewFactory(NodeViewFactory nodeViewFactory) {
        getDrawing().setNodeViewFactory(nodeViewFactory);
    }

    @Override
    default Property<Node> rootNodeProperty() {
        return getDrawing().rootNodeProperty();
    }

    @Override
    default void setRootNode(Node rootNode) {
        getDrawing().setRootNode(rootNode);
    }

    @Override
    default Node getRootNode() {
        return getDrawing().getRootNode();
    }
}
