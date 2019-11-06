package webfx.kit.mapper.peers.javafxgraphics.markers;

import javafx.beans.property.DoubleProperty;

/**
 * @author Bruno Salmon
 */
public interface HasWidthProperty {

    DoubleProperty widthProperty();
    default void setWidth(Number width) { widthProperty().setValue(width); }
    default Double getWidth() { return widthProperty().getValue(); }

}
