package naga.toolkit.fx.properties.markers;

import javafx.beans.property.Property;

/**
 * @author Bruno Salmon
 */
public interface HasPivotYProperty {

    Property<Double> pivotYProperty();
    default void setPivotY(Double pivotY) { pivotYProperty().setValue(pivotY); }
    default Double getPivotY() { return pivotYProperty().getValue(); }

}
