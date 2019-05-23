package webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.markers;

import javafx.beans.property.DoubleProperty;

/**
 * @author Bruno Salmon
 */
public interface HasMinWidthProperty {

    DoubleProperty minWidthProperty();
    default void setMinWidth(Number minWidth) { minWidthProperty().setValue(minWidth); }
    default Double getMinWidth() { return minWidthProperty().getValue(); }

}
