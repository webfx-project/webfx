package webfx.fx.properties.markers;

import emul.javafx.beans.property.Property;
import emul.javafx.scene.Parent;

/**
 * @author Bruno Salmon
 */
public interface HasParentProperty {

    Property<Parent> parentProperty();
    default void setParent(Parent parent) {
        parentProperty().setValue(parent);
    }
    default Parent getParent() {
        return parentProperty().getValue();
    }

}
