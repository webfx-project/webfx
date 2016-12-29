package naga.fx.spi.viewer.base;

import javafx.beans.property.Property;
import naga.fx.scene.control.Slider;

/**
 * @author Bruno Salmon
 */
public interface SliderViewerMixin
        <N extends Slider, NB extends SliderViewerBase<N, NB, NM>, NM extends SliderViewerMixin<N, NB, NM>>

        extends ControlViewerMixin<N, NB, NM> {

    void updateMin(Double min);

    void updateMax(Double max);

    void updateValue(Double value);

    default void updateNodeValue(Double value) {
        Property<Double> valueProperty = getNodeViewerBase().getNode().valueProperty();
        if (!valueProperty.isBound())
            valueProperty.setValue(value);
    }

}
