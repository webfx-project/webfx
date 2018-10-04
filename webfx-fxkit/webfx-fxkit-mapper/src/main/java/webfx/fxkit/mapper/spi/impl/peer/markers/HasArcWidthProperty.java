package webfx.fxkit.mapper.spi.impl.peer.markers;

import javafx.beans.property.Property;

/**
 * @author Bruno Salmon
 */
public interface HasArcWidthProperty {

    Property<Double> arcWidthProperty();
    default void setArcWidth(Double arcWidth) { arcWidthProperty().setValue(arcWidth); }
    default Double getArcWidth() { return arcWidthProperty().getValue(); }

}
