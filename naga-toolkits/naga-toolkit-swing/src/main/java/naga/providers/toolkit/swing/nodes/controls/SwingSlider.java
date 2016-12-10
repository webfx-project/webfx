package naga.providers.toolkit.swing.nodes.controls;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.providers.toolkit.swing.nodes.SwingNode;
import naga.toolkit.spi.nodes.controls.Slider;

import javax.swing.*;

/**
 * @author Bruno Salmon
 */
public class SwingSlider extends SwingNode<JSlider> implements Slider {

    public SwingSlider() {
        this(createSlider());
    }

    public SwingSlider(JSlider slider) {
        super(slider);
        minProperty.addListener((observable, oldValue, newValue) -> node.setMinimum(newValue.intValue()));
        maxProperty.addListener((observable, oldValue, newValue) -> node.setMaximum(newValue.intValue()));
        valueProperty.addListener((observable, oldValue, newValue) -> node.setValue(newValue.intValue()));
        node.addChangeListener(e -> {
            if (!valueProperty.isBound())
                valueProperty.setValue((double) node.getValue());
        });
    }

    private static JSlider createSlider() {
        JSlider slider = new JSlider();
        slider.setMinorTickSpacing(50);
        slider.setMajorTickSpacing(500);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setValue(0);
        //slider.setPaintTrack(true);
/*
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(true);
        slider.setMinorTickCount(4);
        slider.setMajorTickUnit(500);
*/
        return slider;
    }

    private Property<Double> valueProperty = new SimpleObjectProperty<>(0d);
    @Override
    public Property<Double> valueProperty() {
        return valueProperty;
    }

    private Property<Double> minProperty = new SimpleObjectProperty<>(0d);
    @Override
    public Property<Double> minProperty() {
        return minProperty;
    }

    private Property<Double> maxProperty = new SimpleObjectProperty<>(100d);
    @Override
    public Property<Double> maxProperty() {
        return maxProperty;
    }
}
