package naga.fx.properties.markers;

import emul.javafx.beans.property.Property;

/**
 * @author Bruno Salmon
 */
public interface HasMinWidthProperty {

    Property<Double> minWidthProperty();
    default void setMinWidth(Double minWidth) { minWidthProperty().setValue(minWidth); }
    default Double getMinWidth() { return minWidthProperty().getValue(); }

}
