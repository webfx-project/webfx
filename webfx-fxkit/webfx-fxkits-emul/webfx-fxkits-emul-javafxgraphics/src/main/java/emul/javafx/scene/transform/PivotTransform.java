package emul.javafx.scene.transform;

import emul.javafx.beans.property.Property;
import emul.javafx.beans.property.SimpleObjectProperty;
import naga.fx.properties.markers.HasPivotXProperty;
import naga.fx.properties.markers.HasPivotYProperty;

/**
 * @author Bruno Salmon
 */
abstract class PivotTransform extends Transform implements
        HasPivotXProperty,
        HasPivotYProperty {

    PivotTransform() {
    }

    PivotTransform(double pivotX, double pivotY) {
        setPivotX(pivotX);
        setPivotY(pivotY);
    }

    final Property<Double> pivotXProperty = new SimpleObjectProperty<>(0d);
    @Override
    public Property<Double> pivotXProperty() {
        return pivotXProperty;
    }

    final Property<Double> pivotYProperty = new SimpleObjectProperty<>(0d);
    @Override
    public Property<Double> pivotYProperty() {
        return pivotYProperty;
    }


}
