package emul.javafx.scene.control;

import emul.javafx.beans.property.Property;
import emul.javafx.beans.property.SimpleObjectProperty;
import naga.fx.properties.markers.HasMaxProperty;
import naga.fx.properties.markers.HasMinProperty;
import naga.fx.properties.markers.HasValueProperty;

/**
 * @author Bruno Salmon
 */
public class Slider extends Control implements
        HasMinProperty,
        HasValueProperty,
        HasMaxProperty {

    public Slider() {
    }

    /**
     * Constructs a Slider control with the specified slider min, max and current value values.
     * @param min Slider minimum value
     * @param max Slider maximum value
     * @param value Slider current value
     */
    public Slider(double min, double max, double value) {
        setMax(max);
        setMin(min);
        setValue(value);
        //adjustValues();
        //initialize();
    }
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
