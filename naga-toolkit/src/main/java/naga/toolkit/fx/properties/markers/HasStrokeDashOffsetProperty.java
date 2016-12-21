package naga.toolkit.fx.properties.markers;

import javafx.beans.property.Property;

/**
 * @author Bruno Salmon
 */
public interface HasStrokeDashOffsetProperty {

    Property<Double> strokeDashOffsetProperty();
    default void setStrokeDashOffset(Double strokeDashOffset) { strokeDashOffsetProperty().setValue(strokeDashOffset); }
    default Double getStrokeDashOffset() { return strokeDashOffsetProperty().getValue(); }

}
