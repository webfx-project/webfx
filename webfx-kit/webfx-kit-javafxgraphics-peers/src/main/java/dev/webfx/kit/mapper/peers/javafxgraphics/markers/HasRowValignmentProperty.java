package dev.webfx.kit.mapper.peers.javafxgraphics.markers;

import javafx.beans.property.ObjectProperty;
import javafx.geometry.VPos;

/**
 * @author Bruno Salmon
 */
public interface HasRowValignmentProperty {

    ObjectProperty<VPos> rowValignmentProperty();
    default void setRowValignment(VPos rowValignment) {
        rowValignmentProperty().setValue(rowValignment);
    }
    default VPos getRowValignment() {
        return rowValignmentProperty().getValue();
    }

}
