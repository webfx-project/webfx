package webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.markers;

import javafx.beans.property.DoubleProperty;

/**
 * @author Bruno Salmon
 */
public interface HasMinHeightProperty {

    DoubleProperty minHeightProperty();
    default void setMinHeight(Number minHeight) { minHeightProperty().setValue(minHeight); }
    default Double getMinHeight() { return minHeightProperty().getValue(); }

}
