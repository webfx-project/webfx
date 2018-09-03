package webfx.fxkits.core.properties.markers;

import emul.javafx.beans.property.Property;
import emul.javafx.scene.Node;

/**
 * @author Bruno Salmon
 */
public interface HasGraphicProperty {

    Property<Node> graphicProperty();
    default HasGraphicProperty setGraphic(Node graphic) { graphicProperty().setValue(graphic); return this; }
    default Node getGraphic() { return graphicProperty().getValue(); }
}
