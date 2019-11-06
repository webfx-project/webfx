package webfx.kit.mapper.peers.javafxgraphics.markers;

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
