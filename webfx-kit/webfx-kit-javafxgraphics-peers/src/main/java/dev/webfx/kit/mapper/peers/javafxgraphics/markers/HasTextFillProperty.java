package dev.webfx.kit.mapper.peers.javafxgraphics.markers;

import javafx.beans.property.ObjectProperty;
import javafx.scene.paint.Paint;

/**
 * @author Bruno Salmon
 */
public interface HasTextFillProperty {

    ObjectProperty<Paint> textFillProperty();
    default void setTextFill(Paint textFill) {
        textFillProperty().setValue(textFill);
    }
    default Paint getTextFill() {
        return textFillProperty().getValue();
    }
}
