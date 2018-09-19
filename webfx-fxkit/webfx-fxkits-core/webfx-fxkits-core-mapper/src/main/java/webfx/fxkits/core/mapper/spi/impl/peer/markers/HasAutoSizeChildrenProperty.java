package webfx.fxkits.core.mapper.spi.impl.peer.markers;


import javafx.beans.property.Property;

/**
 * @author Bruno Salmon
 */
public interface HasAutoSizeChildrenProperty {

    Property<Boolean> autoSizeChildrenProperty();
    default void setAutoSizeChildren(Boolean autoSizeChildren) { autoSizeChildrenProperty().setValue(autoSizeChildren); }
    default Boolean isAutoSizeChildren() { return autoSizeChildrenProperty().getValue(); }

}
