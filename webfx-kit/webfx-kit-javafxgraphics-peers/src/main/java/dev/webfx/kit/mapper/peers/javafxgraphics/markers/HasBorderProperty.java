package dev.webfx.kit.mapper.peers.javafxgraphics.markers;

import javafx.beans.property.ObjectProperty;
import javafx.scene.layout.Border;

/**
 * @author Bruno Salmon
 */
public interface HasBorderProperty {

    ObjectProperty<Border> borderProperty();
    default void setBorder(Border border) { borderProperty().setValue(border); }
    default Border getBorder() { return borderProperty().getValue(); }

}
