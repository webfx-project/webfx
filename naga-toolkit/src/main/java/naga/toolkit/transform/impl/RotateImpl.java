package naga.toolkit.transform.impl;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.toolkit.transform.Rotate;

/**
 * @author Bruno Salmon
 */
public class RotateImpl extends TransformImpl implements Rotate {

    private final Property<Double> angleProperty = new SimpleObjectProperty<>(0d);
    @Override
    public Property<Double> angleProperty() {
        return angleProperty;
    }

    private final Property<Double> pivotXProperty = new SimpleObjectProperty<>(0d);
    @Override
    public Property<Double> pivotXProperty() {
        return pivotXProperty;
    }

    private final Property<Double> pivotYProperty = new SimpleObjectProperty<>(0d);
    @Override
    public Property<Double> pivotYProperty() {
        return pivotYProperty;
    }
}
