package webfx.fxkits.core.properties.markers;

import emul.javafx.beans.property.Property;
import emul.javafx.scene.Parent;

/**
 * @author Bruno Salmon
 */
public interface HasRootProperty {

    Property<Parent> rootProperty();
    default void setRoot(Parent root) { rootProperty().setValue(root); }
    default Parent getRoot() { return rootProperty().getValue(); }
}
