package webfx.fxkits.core.mapper.spi.impl.peer.markers;

import javafx.beans.property.Property;

/**
 * @author Bruno Salmon
 */
public interface HasHeightProperty {

    Property<Double> heightProperty();
    default void setHeight(Double height) { heightProperty().setValue(height); }
    default Double getHeight() { return heightProperty().getValue(); }

}
