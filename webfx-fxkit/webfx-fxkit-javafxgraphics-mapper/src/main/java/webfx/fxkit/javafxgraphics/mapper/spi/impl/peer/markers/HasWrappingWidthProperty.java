package webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.markers;

import javafx.beans.property.DoubleProperty;

/**
 * @author Bruno Salmon
 */
public interface HasWrappingWidthProperty {

    DoubleProperty wrappingWidthProperty();
    default void setWrappingWidth(Number wrappingWidth) { wrappingWidthProperty().setValue(wrappingWidth); }
    default Double getWrappingWidth() { return wrappingWidthProperty().getValue(); }

}
