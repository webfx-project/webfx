package webfx.kit.mapper.peers.javafxgraphics.markers;

import javafx.beans.property.DoubleProperty;

/**
 * @author Bruno Salmon
 */
public interface HasValueProperty {

    DoubleProperty valueProperty();
    default void setValue(Number value) { valueProperty().setValue(value); }
    default Double getValue() { return valueProperty().getValue(); }

}
