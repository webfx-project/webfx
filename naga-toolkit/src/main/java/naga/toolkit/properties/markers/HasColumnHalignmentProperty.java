package naga.toolkit.properties.markers;

import javafx.beans.property.Property;
import naga.toolkit.drawing.geometry.HPos;

/**
 * @author Bruno Salmon
 */
public interface HasColumnHalignmentProperty {

    Property<HPos> columnHalignmentProperty();
    default void setColumnHalignment(HPos columnHalignment) {
        columnHalignmentProperty().setValue(columnHalignment);
    }
    default HPos getColumnHalignment() {
        return columnHalignmentProperty().getValue();
    }

}
