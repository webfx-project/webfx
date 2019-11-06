package webfx.kit.mapper.peers.javafxgraphics.markers;

import javafx.beans.property.Property;
import javafx.scene.paint.Paint;

/**
 * @author Bruno Salmon
 */
public interface HasTextFillProperty {

    Property<Paint> textFillProperty();
    default void setTextFill(Paint textFill) {
        textFillProperty().setValue(textFill);
    }
    default Paint getTextFill() {
        return textFillProperty().getValue();
    }
}
