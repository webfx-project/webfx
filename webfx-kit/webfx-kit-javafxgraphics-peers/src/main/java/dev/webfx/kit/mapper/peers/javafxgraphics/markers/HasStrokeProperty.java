package dev.webfx.kit.mapper.peers.javafxgraphics.markers;

import javafx.beans.property.ObjectProperty;
import javafx.scene.paint.Paint;

/**
 * @author Bruno Salmon
 */
public interface HasStrokeProperty {

    ObjectProperty<Paint> strokeProperty();
    default void setStroke(Paint stroke) {
        strokeProperty().setValue(stroke);
    }
    default Paint getStroke() {
        return strokeProperty().getValue();
    }
}
