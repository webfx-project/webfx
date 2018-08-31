package webfx.fx.properties.markers;

import emul.javafx.beans.property.Property;

/**
 * @author Bruno Salmon
 */
public interface HasVgapProperty {

    Property<Double> vgapProperty();
    default void setVgap(Double vgap) { vgapProperty().setValue(vgap); }
    default Double getVgap() { return vgapProperty().getValue(); }

}
