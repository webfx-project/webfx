package webfx.fxkits.core.properties.markers;

import emul.javafx.beans.property.Property;
import emul.javafx.scene.Node;

/**
 * @author Bruno Salmon
 */
public interface HasClipProperty {

    Property<Node> clipProperty();
    default void setClip(Node node) { clipProperty().setValue(node); }
    default Node getClip() { return clipProperty().getValue(); }
}
