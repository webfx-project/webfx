package webfx.fxkit.mapper.spi.impl.peer.markers;

import javafx.beans.property.DoubleProperty;

/**
 * @author Bruno Salmon
 */
public interface HasArcHeightProperty {

    DoubleProperty arcHeightProperty();
    default void setArcHeight(Number arcHeight) { arcHeightProperty().setValue(arcHeight); }
    default Double getArcHeight() { return arcHeightProperty().getValue(); }

}
