package javafx.scene.transform;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import webfx.fxkit.mapper.spi.impl.peer.markers.HasPivotXProperty;
import webfx.fxkit.mapper.spi.impl.peer.markers.HasPivotYProperty;

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
