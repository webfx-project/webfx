package naga.core.spi.toolkit.gwtpolymer.controls;

import com.vaadin.polymer.Polymer;
import com.vaadin.polymer.paper.widget.PaperSlider;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.core.spi.toolkit.controls.Slider;
import naga.core.spi.toolkit.gwt.node.GwtNode;

/**
 * @author Bruno Salmon
 */
public class GwtPolymerSlider extends GwtNode<PaperSlider> implements Slider<PaperSlider> {

    public GwtPolymerSlider() {
        this(createPaperSlider());
    }

    public GwtPolymerSlider(PaperSlider slider) {
        super(slider);
        Polymer.ready(slider.getElement(), o -> {
            if (getMin() != null)
                slider.setMin(getMin());
            minProperty().addListener((observable, oldMin, newMin) -> slider.setMin(newMin));
            if (getMax() != null)
                slider.setMax(getMax());
            maxProperty().addListener((observable, oldMax, newMax) -> slider.setMax(newMax));
            if (getValue() != null)
                slider.setValue(getValue());
            valueProperty().addListener((observable, oldValue, newValue) -> syncValueToVisual());
            slider.addChangeHandler(valueChangeEvent -> syncValueFromVisual());
            slider.addImmediateValueChangeHandler(valueChangeEvent -> syncValueFromVisual());
            return null;
        });
    }

    private static PaperSlider createPaperSlider() {
        PaperSlider paperSlider = new PaperSlider();
        paperSlider.setPin(true);
        return paperSlider;
    }

    private void syncValueToVisual() {
        node.setValue(getValue());
    }

    private void syncValueFromVisual() {
        // If the value property is not bound, we update it to reflect the visual value that the user just changed
        if (!valueProperty().isBound())
            setValue((int) node.getImmediateValue());
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
