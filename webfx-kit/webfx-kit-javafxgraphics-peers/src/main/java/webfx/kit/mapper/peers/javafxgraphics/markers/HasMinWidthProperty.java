package webfx.kit.mapper.peers.javafxgraphics.markers;

import javafx.beans.property.DoubleProperty;

/**
 * @author Bruno Salmon
 */
public interface HasMinWidthProperty {

    DoubleProperty minWidthProperty();
    default void setMinWidth(Number minWidth) { minWidthProperty().setValue(minWidth); }
    default Double getMinWidth() { return minWidthProperty().getValue(); }

}
