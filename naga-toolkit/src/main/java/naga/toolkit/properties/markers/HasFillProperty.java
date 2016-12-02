package naga.toolkit.properties.markers;

import javafx.beans.property.Property;
import naga.toolkit.fx.scene.paint.Paint;

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
