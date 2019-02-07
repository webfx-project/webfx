package webfx.fxkit.mapper.spi.impl.peer.markers;

import javafx.beans.property.DoubleProperty;

/**
 * @author Bruno Salmon
 */
public interface HasArcWidthProperty {

    DoubleProperty arcWidthProperty();
    default void setArcWidth(Number arcWidth) { arcWidthProperty().setValue(arcWidth); }
    default Double getArcWidth() { return arcWidthProperty().getValue(); }

}
