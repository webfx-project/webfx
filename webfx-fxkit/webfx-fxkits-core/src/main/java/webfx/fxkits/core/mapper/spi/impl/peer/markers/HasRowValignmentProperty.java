package webfx.fxkits.core.mapper.spi.impl.peer.markers;

import javafx.beans.property.Property;
import javafx.geometry.VPos;

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
