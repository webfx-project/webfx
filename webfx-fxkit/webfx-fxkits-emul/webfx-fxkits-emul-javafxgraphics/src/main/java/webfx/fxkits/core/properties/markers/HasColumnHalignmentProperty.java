package webfx.fxkits.core.properties.markers;

import emul.javafx.beans.property.Property;
import emul.javafx.geometry.HPos;

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
