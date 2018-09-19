package webfx.fxkits.core.mapper.spi.impl.peer.markers;


import javafx.beans.property.Property;

/**
 * @author Bruno Salmon
 */
public interface HasVisibleProperty {

    Property<Boolean> visibleProperty();
    default void setVisible(Boolean visible) { visibleProperty().setValue(visible); }
    default Boolean isVisible() { return visibleProperty().getValue(); }

}
