package webfx.fxkit.mapper.spi.impl.peer.markers;

import javafx.beans.property.DoubleProperty;

/**
 * @author Bruno Salmon
 */
public interface HasMaxHeightProperty {

    DoubleProperty maxHeightProperty();
    default void setMaxHeight(Number maxHeight) { maxHeightProperty().setValue(maxHeight); }
    default Double getMaxHeight() { return maxHeightProperty().getValue(); }

}
