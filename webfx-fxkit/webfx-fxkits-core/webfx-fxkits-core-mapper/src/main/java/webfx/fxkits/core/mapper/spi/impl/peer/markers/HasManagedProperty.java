package webfx.fxkits.core.mapper.spi.impl.peer.markers;


import javafx.beans.property.Property;

/**
 * @author Bruno Salmon
 */
public interface HasManagedProperty {

    Property<Boolean> managedProperty();
    default void setManaged(Boolean managed) { managedProperty().setValue(managed); }
    default Boolean isManaged() { return managedProperty().getValue(); }

}
