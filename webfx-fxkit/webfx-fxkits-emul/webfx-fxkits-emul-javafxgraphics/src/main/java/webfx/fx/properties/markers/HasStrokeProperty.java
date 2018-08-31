package webfx.fx.properties.markers;

import emul.javafx.beans.property.Property;
import emul.javafx.scene.paint.Paint;

/**
 * @author Bruno Salmon
 */
public interface HasStrokeProperty {

    Property<Paint> strokeProperty();
    default void setStroke(Paint stroke) {
        strokeProperty().setValue(stroke);
    }
    default Paint getStroke() {
        return strokeProperty().getValue();
    }
}
