package naga.core.spi.toolkit.pivot.controls;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.core.spi.toolkit.controls.Slider;
import naga.core.spi.toolkit.pivot.node.PivotNode;

/**
 * @author Bruno Salmon
 */
public class PivotSlider extends PivotNode<org.apache.pivot.wtk.Slider> implements Slider<org.apache.pivot.wtk.Slider> {

    public PivotSlider() {
        this(createPivotSlider());
    }

    public PivotSlider(org.apache.pivot.wtk.Slider slider) {
        super(slider);
        if (minProperty().getValue() != null)
            slider.setStart(getMin());
        minProperty().addListener((observable, oldMin, newMin) -> slider.setStart(newMin));
        if (maxProperty().getValue() != null)
            slider.setEnd(getMax());
        maxProperty().addListener((observable, oldMax, newMax) -> slider.setEnd(newMax));
        if (valueProperty().getValue() != null)
            slider.setValue(getValue());
        valueProperty().addListener((observable, oldValue, newValue) -> syncValueToVisual());
        slider.getSliderValueListeners().add((slider1, previousValue) -> syncValueFromVisual());
    }

    private static org.apache.pivot.wtk.Slider createPivotSlider() {
        org.apache.pivot.wtk.Slider pivotSlider = new org.apache.pivot.wtk.Slider();
        return pivotSlider;
    }

    private void syncValueToVisual() {
        node.setValue(getValue());
    }

    private void syncValueFromVisual() {
        // If the value property is not bound, we update it to reflect the visual value that the user just changed
        if (!valueProperty().isBound())
            setValue(node.getValue());
        else // otherwise (meaning the user tried to change the value whereas he shouldn't since the property is bound)
            syncValueToVisual(); // we do the opposite: we reset the slider value from the value property
    }

    private final Property<Integer> maxProperty = new SimpleObjectProperty<>();

    @Override
    public Property<Integer> maxProperty() {
        return maxProperty;
    }

    private final Property<Integer> minProperty = new SimpleObjectProperty<>();

    @Override
    public Property<Integer> minProperty() {
        return minProperty;
    }

    private final Property<Integer> valueProperty = new SimpleObjectProperty<>();

    @Override
    public Property<Integer> valueProperty() {
        return valueProperty;
    }
}
