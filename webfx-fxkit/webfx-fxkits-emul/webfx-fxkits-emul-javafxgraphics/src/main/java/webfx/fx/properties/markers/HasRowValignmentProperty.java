package webfx.fx.properties.markers;

import emul.javafx.beans.property.Property;
import emul.javafx.geometry.VPos;

/**
 * @author Bruno Salmon
 */
public interface HasRowValignmentProperty {

    Property<VPos> rowValignmentProperty();
    default void setRowValignment(VPos rowValignment) {
        rowValignmentProperty().setValue(rowValignment);
    }
    default VPos getRowValignment() {
        return rowValignmentProperty().getValue();
    }

}
