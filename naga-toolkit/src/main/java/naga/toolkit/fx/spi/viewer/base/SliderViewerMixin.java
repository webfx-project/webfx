package naga.toolkit.fx.spi.viewer.base;

import javafx.beans.property.Property;
import naga.toolkit.fx.scene.control.Slider;
import naga.toolkit.fx.spi.viewer.SliderViewer;

/**
 * @author Bruno Salmon
 */
public interface SliderViewerMixin
        extends SliderViewer,
        ControlViewerMixin<Slider, SliderViewerBase, SliderViewerMixin> {

    void updateMin(Double min);

    void updateMax(Double max);

    void updateValue(Double value);

    default void updateNodeValue(Double value) {
        Property<Double> valueProperty = getNodeViewerBase().getNode().valueProperty();
        if (!valueProperty.isBound())
            valueProperty.setValue(value);
    }

}
