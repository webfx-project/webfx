package naga.providers.toolkit.javafx.nodes.controls;

import javafx.beans.property.Property;
import javafx.scene.control.Slider;
import naga.providers.toolkit.javafx.JavaFxToolkit;
import naga.providers.toolkit.javafx.nodes.FxNode;
import naga.toolkit.properties.conversion.ConvertedProperty;

/**
 * @author Bruno Salmon
 */
public class FxSlider extends FxNode<Slider> implements naga.toolkit.spi.nodes.controls.Slider<javafx.scene.control.Slider> {

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


    private ConvertedProperty<Integer, Number> valueProperty = JavaFxToolkit.numberToIntegerProperty(node.valueProperty());
    @Override
    public Property<Integer> valueProperty() {
        return valueProperty;
    }

    private ConvertedProperty<Integer, Number> minProperty = JavaFxToolkit.numberToIntegerProperty(node.minProperty());
    @Override
    public Property<Integer> minProperty() {
        return minProperty;
    }

    private ConvertedProperty<Integer, Number> maxProperty = JavaFxToolkit.numberToIntegerProperty(node.maxProperty());
    @Override
    public Property<Integer> maxProperty() {
        return maxProperty;
    }

}
