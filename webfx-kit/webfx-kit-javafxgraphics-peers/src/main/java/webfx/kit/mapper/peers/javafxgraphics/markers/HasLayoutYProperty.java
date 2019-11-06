package webfx.kit.mapper.peers.javafxgraphics.markers;

import javafx.beans.property.DoubleProperty;

/**
 * @author Bruno Salmon
 */
public interface HasLayoutYProperty {

    DoubleProperty layoutYProperty();
    default void setLayoutY(Double layoutY) { layoutYProperty().setValue(layoutY); }
    default Double getLayoutY() { return layoutYProperty().getValue(); }

}
