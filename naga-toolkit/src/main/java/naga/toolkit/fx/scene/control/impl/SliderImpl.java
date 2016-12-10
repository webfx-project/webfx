package naga.toolkit.fx.scene.control.impl;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.toolkit.fx.scene.control.Slider;

/**
 * @author Bruno Salmon
 */
public class SliderImpl extends ControlImpl implements Slider {

    private final Property<Double> maxProperty = new SimpleObjectProperty<>(100d);
    @Override
    public Property<Double> maxProperty() {
        return maxProperty;
    }

    private final Property<Double> minProperty = new SimpleObjectProperty<>(0d);
    @Override
    public Property<Double> minProperty() {
        return minProperty;
    }

    private final Property<Double> valueProperty = new SimpleObjectProperty<>(0d);
    @Override
    public Property<Double> valueProperty() {
        return valueProperty;
    }
}
