package webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.markers;

import javafx.beans.property.DoubleProperty;

/**
 * @author Bruno Salmon
 */
public interface HasOpacityProperty {

    DoubleProperty opacityProperty();
    default void setOpacity(Number opacity) { opacityProperty().setValue(opacity); }
    default Double getOpacity() { return opacityProperty().getValue(); }

}
