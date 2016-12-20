package naga.toolkit.fx.spi.viewer.base;

import javafx.beans.property.Property;
import naga.toolkit.fx.scene.control.Slider;

/**
 * @author Bruno Salmon
 */
public interface SliderViewerMixin
        <N extends Slider, NV extends SliderViewerBase<N, NV, NM>, NM extends SliderViewerMixin<N, NV, NM>>

        extends ControlViewerMixin<N, NV, NM> {

    void updateMin(Double min);

    void updateMax(Double max);

    void updateValue(Double value);

    default void updateNodeValue(Double value) {
        Property<Double> valueProperty = getNodeViewerBase().getNode().valueProperty();
        if (!valueProperty.isBound())
            valueProperty.setValue(value);
    }

}
