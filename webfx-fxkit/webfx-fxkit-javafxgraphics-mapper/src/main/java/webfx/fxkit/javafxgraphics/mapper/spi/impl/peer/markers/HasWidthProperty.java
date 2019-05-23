package webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.markers;

import javafx.beans.property.DoubleProperty;

/**
 * @author Bruno Salmon
 */
public interface HasWidthProperty {

    DoubleProperty widthProperty();
    default void setWidth(Number width) { widthProperty().setValue(width); }
    default Double getWidth() { return widthProperty().getValue(); }

}
