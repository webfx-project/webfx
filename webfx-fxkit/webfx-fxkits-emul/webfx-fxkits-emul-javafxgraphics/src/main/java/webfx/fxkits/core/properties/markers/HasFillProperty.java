package webfx.fxkits.core.properties.markers;

import emul.javafx.beans.property.Property;
import emul.javafx.scene.paint.Paint;

/**
 * @author Bruno Salmon
 */
public interface HasFillProperty {

    Property<Paint> fillProperty();
    default void setFill(Paint fill) {
        fillProperty().setValue(fill);
    }
    default Paint getFill() {
        return fillProperty().getValue();
    }
}
