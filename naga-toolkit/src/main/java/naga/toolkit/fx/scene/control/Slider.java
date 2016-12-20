package naga.toolkit.fx.scene.control;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.toolkit.properties.markers.HasMaxProperty;
import naga.toolkit.properties.markers.HasMinProperty;
import naga.toolkit.properties.markers.HasValueProperty;

/**
 * @author Bruno Salmon
 */
public class Slider extends Control implements
        HasMinProperty,
        HasValueProperty,
        HasMaxProperty {

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
