package webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.markers;

import javafx.beans.property.DoubleProperty;

/**
 * @author Bruno Salmon
 */
public interface HasPrefWrapLengthProperty {

    DoubleProperty prefWrapLengthProperty();
    default void setPrefWrapLength(Number prefWrapLength) { prefWrapLengthProperty().setValue(prefWrapLength); }
    default Double getPrefWrapLength() { return prefWrapLengthProperty().getValue(); }

}
