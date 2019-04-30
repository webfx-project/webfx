package webfx.fxkit.mapper.spi.impl.peer.markers;

import javafx.beans.property.DoubleProperty;

/**
 * @author Bruno Salmon
 */
public interface HasMaxWidthProperty {

    DoubleProperty maxWidthProperty();
    default void setMaxWidth(Number maxWidth) { maxWidthProperty().setValue(maxWidth); }
    default Double getMaxWidth() { return maxWidthProperty().getValue(); }

}
