package webfx.fxkits.core.properties.markers;

import emul.javafx.beans.property.Property;
import emul.javafx.scene.paint.Paint;

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
