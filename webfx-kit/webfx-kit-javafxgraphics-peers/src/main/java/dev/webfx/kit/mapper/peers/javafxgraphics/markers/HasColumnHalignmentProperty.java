package dev.webfx.kit.mapper.peers.javafxgraphics.markers;

import javafx.beans.property.ObjectProperty;
import javafx.geometry.HPos;

/**
 * @author Bruno Salmon
 */
public interface HasColumnHalignmentProperty {

    ObjectProperty<HPos> columnHalignmentProperty();
    default void setColumnHalignment(HPos columnHalignment) {
        columnHalignmentProperty().setValue(columnHalignment);
    }
    default HPos getColumnHalignment() {
        return columnHalignmentProperty().getValue();
    }

}
