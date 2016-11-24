package naga.toolkit.properties.markers;

import javafx.beans.property.Property;
import naga.toolkit.drawing.geometry.Insets;

/**
 * @author Bruno Salmon
 */
public interface HasInsetsProperty {

    Property<Insets> insetsProperty();
    default void setInsets(Insets insets) {
        insetsProperty().setValue(insets);
    }
    default Insets getInsets() {
        return insetsProperty().getValue();
    }

}
