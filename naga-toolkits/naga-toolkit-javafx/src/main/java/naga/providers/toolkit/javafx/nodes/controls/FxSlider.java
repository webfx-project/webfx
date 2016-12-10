package naga.providers.toolkit.javafx.nodes.controls;

import javafx.beans.property.Property;
import javafx.scene.control.Slider;
import naga.providers.toolkit.javafx.nodes.FxNode;
import naga.toolkit.properties.conversion.ConvertedProperty;

/**
 * @author Bruno Salmon
 */
public class FxSlider extends FxNode<Slider> implements naga.toolkit.spi.nodes.controls.Slider {

    public FxSlider() {
        this(createSlider());
    }

    public FxSlider(javafx.scene.control.Slider slider) {
        super(slider);
    }

    private static javafx.scene.control.Slider createSlider() {
        javafx.scene.control.Slider slider = new javafx.scene.control.Slider();
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(true);
        slider.setMinorTickCount(4);
        slider.setMajorTickUnit(500);
        return slider;
    }


    private ConvertedProperty<Double, Number> valueProperty = ConvertedProperty.numberToDoubleProperty(node.valueProperty());
    @Override
    public Property<Double> valueProperty() {
        return valueProperty;
    }

    private ConvertedProperty<Double, Number> minProperty = ConvertedProperty.numberToDoubleProperty(node.minProperty());
    @Override
    public Property<Double> minProperty() {
        return minProperty;
    }

    private ConvertedProperty<Double, Number> maxProperty = ConvertedProperty.numberToDoubleProperty(node.maxProperty());
    @Override
    public Property<Double> maxProperty() {
        return maxProperty;
    }

}
