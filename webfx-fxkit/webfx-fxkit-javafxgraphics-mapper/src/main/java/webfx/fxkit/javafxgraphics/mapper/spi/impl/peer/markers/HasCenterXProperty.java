package webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.markers;

import javafx.beans.property.DoubleProperty;

/**
 * @author Bruno Salmon
 */
public interface HasCenterXProperty {

    DoubleProperty centerXProperty();

    default void setCenterX(Number centerX) {
        centerXProperty().setValue(centerX);
    }

    default Double getCenterX() {
        return centerXProperty().getValue();
    }

}
