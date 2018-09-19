package webfx.fxkits.core.mapper.spi.impl.peer.markers;

import javafx.beans.property.Property;

/**
 * @author Bruno Salmon
 */
public interface HasWidthProperty {

    Property<Double> widthProperty();
    default void setWidth(Double width) { widthProperty().setValue(width); }
    default Double getWidth() { return widthProperty().getValue(); }

}
