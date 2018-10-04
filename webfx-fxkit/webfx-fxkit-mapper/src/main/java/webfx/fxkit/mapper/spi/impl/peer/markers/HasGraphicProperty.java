package webfx.fxkit.mapper.spi.impl.peer.markers;

import javafx.beans.property.Property;
import javafx.scene.Node;

/**
 * @author Bruno Salmon
 */
public interface HasGraphicProperty {

    Property<Node> graphicProperty();
    default HasGraphicProperty setGraphic(Node graphic) { graphicProperty().setValue(graphic); return this; }
    default Node getGraphic() { return graphicProperty().getValue(); }
}
