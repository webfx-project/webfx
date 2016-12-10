package naga.providers.toolkit.pivot.nodes.controls;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.toolkit.spi.nodes.controls.Slider;
import naga.providers.toolkit.pivot.nodes.PivotNode;

/**
 * @author Bruno Salmon
 */
public class PivotSlider extends PivotNode<org.apache.pivot.wtk.Slider> implements Slider {

    public PivotSlider() {
        this(createPivotSlider());
    }

    public PivotSlider(org.apache.pivot.wtk.Slider slider) {
        super(slider);
        if (minProperty().getValue() != null)
            slider.setStart(getMin().intValue());
        minProperty().addListener((observable, oldMin, newMin) -> slider.setStart(newMin.intValue()));
        if (maxProperty().getValue() != null)
            slider.setEnd(getMax().intValue());
        maxProperty().addListener((observable, oldMax, newMax) -> slider.setEnd(newMax.intValue()));
        if (valueProperty().getValue() != null)
            slider.setValue(getValue().intValue());
        valueProperty().addListener((observable, oldValue, newValue) -> syncValueToVisual());
        slider.getSliderValueListeners().add((slider1, previousValue) -> syncValueFromVisual());
    }

    private static org.apache.pivot.wtk.Slider createPivotSlider() {
        return new org.apache.pivot.wtk.Slider();
    }

    private void syncValueToVisual() {
        node.setValue(getValue().intValue());
    }

    private void syncValueFromVisual() {
        // If the value property is not bound, we update it to reflect the visual value that the user just changed
        if (!valueProperty().isBound())
            setValue((double) node.getValue());
        else // otherwise (meaning the user tried to change the value whereas he shouldn't since the property is bound)
            syncValueToVisual(); // we do the opposite: we reset the slider value from the value property
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
