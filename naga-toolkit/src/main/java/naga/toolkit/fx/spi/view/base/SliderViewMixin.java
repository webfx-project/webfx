package naga.toolkit.fx.spi.view.base;

import javafx.beans.property.Property;
import naga.toolkit.fx.scene.control.Slider;
import naga.toolkit.fx.spi.view.SliderView;

/**
 * @author Bruno Salmon
 */
public interface SliderViewMixin
        extends SliderView,
                ControlViewMixin<Slider, SliderViewBase, SliderViewMixin> {

    void updateMin(Double min);

    void updateMax(Double max);

    void updateValue(Double value);

    default void updateNodeValue(Double value) {
        Property<Double> valueProperty = getNodeViewBase().getNode().valueProperty();
        if (!valueProperty.isBound())
            valueProperty.setValue(value);
    }

}
