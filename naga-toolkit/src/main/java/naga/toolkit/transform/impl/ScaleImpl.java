package naga.toolkit.transform.impl;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.toolkit.transform.Scale;

/**
 * @author Bruno Salmon
 */
public class ScaleImpl extends TransformImpl implements Scale {

    public ScaleImpl() {
    }

    public ScaleImpl(double x, double y) {
        setX(x);
        setY(y);
    }

    private final Property<Double> xProperty = new SimpleObjectProperty<>(1d);
    @Override
    public Property<Double> xProperty() {
        return xProperty;
    }

    private final Property<Double> yProperty = new SimpleObjectProperty<>(1d);
    @Override
    public Property<Double> yProperty() {
        return yProperty;
    }

}
