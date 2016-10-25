package naga.toolkit.properties.markers;

import javafx.beans.property.Property;
import naga.toolkit.drawing.paint.Paint;

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
