package webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.markers;

import javafx.beans.property.DoubleProperty;

/**
 * @author Bruno Salmon
 */
public interface HasStrokeWidthProperty {

    DoubleProperty strokeWidthProperty();
    default void setStrokeWidth(Number strokeWidth) { strokeWidthProperty().setValue(strokeWidth); }
    default Double getStrokeWidth() { return strokeWidthProperty().getValue(); }

}
