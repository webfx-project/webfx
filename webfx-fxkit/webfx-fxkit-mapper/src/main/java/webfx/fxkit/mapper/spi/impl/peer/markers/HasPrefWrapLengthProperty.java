package webfx.fxkit.mapper.spi.impl.peer.markers;

import javafx.beans.property.Property;

/**
 * @author Bruno Salmon
 */
public interface HasPrefWrapLengthProperty {

    Property<Double> prefWrapLengthProperty();
    default void setPrefWrapLength(Double prefWrapLength) { prefWrapLengthProperty().setValue(prefWrapLength); }
    default Double getPrefWrapLength() { return prefWrapLengthProperty().getValue(); }

}
