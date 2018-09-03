package webfx.fxkits.core.properties.markers;

import emul.javafx.beans.property.Property;

/**
 * @author Bruno Salmon
 */
public interface HasStrokeWidthProperty {

    Property<Double> strokeWidthProperty();
    default void setStrokeWidth(Double strokeWidth) { strokeWidthProperty().setValue(strokeWidth); }
    default Double getStrokeWidth() { return strokeWidthProperty().getValue(); }

}
