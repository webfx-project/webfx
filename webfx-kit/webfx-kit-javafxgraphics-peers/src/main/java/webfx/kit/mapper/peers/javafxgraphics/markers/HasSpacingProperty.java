package webfx.kit.mapper.peers.javafxgraphics.markers;

import javafx.beans.property.DoubleProperty;

/**
 * @author Bruno Salmon
 */
public interface HasSpacingProperty {

    DoubleProperty spacingProperty();
    default void setSpacing(Number value) { spacingProperty().setValue(value); }
    default Double getSpacing() { return spacingProperty().getValue(); }

}
