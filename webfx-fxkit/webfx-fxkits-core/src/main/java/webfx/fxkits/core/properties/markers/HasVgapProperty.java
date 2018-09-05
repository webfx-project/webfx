package webfx.fxkits.core.properties.markers;

import javafx.beans.property.Property;

/**
 * @author Bruno Salmon
 */
public interface HasVgapProperty {

    Property<Double> vgapProperty();
    default void setVgap(Double vgap) { vgapProperty().setValue(vgap); }
    default Double getVgap() { return vgapProperty().getValue(); }

}
