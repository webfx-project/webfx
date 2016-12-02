package naga.toolkit.properties.markers;

import javafx.beans.property.Property;
import naga.toolkit.fx.geometry.VPos;

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
