package webfx.fxkits.core.mapper.spi.impl.peer.markers;

import javafx.beans.property.Property;

/**
 * @author Bruno Salmon
 */
public interface HasPrefWidthProperty {

    Property<Double> prefWidthProperty();
    default void setPrefWidth(Double prefWidth) { prefWidthProperty().setValue(prefWidth); }
    default Double getPrefWidth() { return prefWidthProperty().getValue(); }

}
