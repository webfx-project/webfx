package webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.markers;

import javafx.beans.property.DoubleProperty;

/**
 * @author Bruno Salmon
 */
public interface HasHeightProperty {

    DoubleProperty heightProperty();
    default void setHeight(Number height) { heightProperty().setValue(height); }
    default Double getHeight() { return heightProperty().getValue(); }

}
