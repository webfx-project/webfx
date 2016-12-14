package naga.toolkit.properties.markers;

import javafx.beans.property.Property;
import naga.toolkit.fx.scene.Node;

/**
 * @author Bruno Salmon
 */
public interface HasGraphicProperty {

    Property<Node> graphicProperty();
    default HasGraphicProperty setGraphic(Node graphic) { graphicProperty().setValue(graphic); return this; }
    default Node getGraphic() { return graphicProperty().getValue(); }
}
