package webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.markers;

import javafx.beans.property.DoubleProperty;

/**
 * @author Bruno Salmon
 */
public interface HasCenterYProperty {

    DoubleProperty centerYProperty();

    default void setCenterY(Number centerY) {
        centerYProperty().setValue(centerY);
    }

    default Double getCenterY() {
        return centerYProperty().getValue();
    }

}
