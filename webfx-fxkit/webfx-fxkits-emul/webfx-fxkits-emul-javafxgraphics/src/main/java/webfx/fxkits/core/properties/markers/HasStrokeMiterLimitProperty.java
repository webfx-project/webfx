package webfx.fxkits.core.properties.markers;

import emul.javafx.beans.property.Property;

/**
 * @author Bruno Salmon
 */
public interface HasStrokeMiterLimitProperty {

    Property<Double> strokeMiterLimitProperty();
    default void setStrokeMiterLimit(Double strokeMiterLimit) { strokeMiterLimitProperty().setValue(strokeMiterLimit); }
    default Double getStrokeMiterLimit() { return strokeMiterLimitProperty().getValue(); }

}
