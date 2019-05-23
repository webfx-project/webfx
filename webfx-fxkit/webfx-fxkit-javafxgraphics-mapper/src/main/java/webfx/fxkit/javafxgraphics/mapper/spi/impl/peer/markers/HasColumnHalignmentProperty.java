package webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.markers;

import javafx.beans.property.Property;
import javafx.geometry.HPos;

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
