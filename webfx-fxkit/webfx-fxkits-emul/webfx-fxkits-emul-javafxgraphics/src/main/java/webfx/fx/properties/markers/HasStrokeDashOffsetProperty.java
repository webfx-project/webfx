package webfx.fx.properties.markers;

import emul.javafx.beans.property.Property;

/**
 * @author Bruno Salmon
 */
public interface HasStrokeDashOffsetProperty {

    Property<Double> strokeDashOffsetProperty();
    default void setStrokeDashOffset(Double strokeDashOffset) { strokeDashOffsetProperty().setValue(strokeDashOffset); }
    default Double getStrokeDashOffset() { return strokeDashOffsetProperty().getValue(); }

}
