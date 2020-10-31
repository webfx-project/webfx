package webfx.kit.mapper.peers.javafxgraphics.markers;

import javafx.beans.property.DoubleProperty;

/**
 * @author Bruno Salmon
 */
public interface HasHeightProperty {

    DoubleProperty heightProperty();
    default void setHeight(Number height) { heightProperty().setValue(height); }
    default double getHeight() { return heightProperty().getValue(); }

}
