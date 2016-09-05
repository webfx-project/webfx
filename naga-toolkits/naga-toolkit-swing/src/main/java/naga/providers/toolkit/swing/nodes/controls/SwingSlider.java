package naga.providers.toolkit.swing.nodes.controls;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.providers.toolkit.swing.nodes.SwingNode;
import naga.toolkit.spi.nodes.controls.Slider;

import javax.swing.*;

/**
 * @author Bruno Salmon
 */
public class SwingSlider extends SwingNode<JSlider> implements Slider<JSlider> {

    public SwingSlider() {
        this(createSlider());
    }

    public SwingSlider(JSlider slider) {
        super(slider);
        minProperty.addListener((observable, oldValue, newValue) -> node.setMinimum(newValue));
        maxProperty.addListener((observable, oldValue, newValue) -> node.setMaximum(newValue));
        valueProperty.addListener((observable, oldValue, newValue) -> node.setValue(newValue));
        node.addChangeListener(e -> {
            minProperty.setValue(node.getMinimum());
            maxProperty.setValue(node.getMaximum());
            valueProperty.setValue(node.getValue());
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

    private Property<Integer> valueProperty = new SimpleObjectProperty<>(0);
    @Override
    public Property<Integer> valueProperty() {
        return valueProperty;
    }

    private Property<Integer> minProperty = new SimpleObjectProperty<>(0);
    @Override
    public Property<Integer> minProperty() {
        return minProperty;
    }

    private Property<Integer> maxProperty = new SimpleObjectProperty<>(0);
    @Override
    public Property<Integer> maxProperty() {
        return maxProperty;
    }

}
