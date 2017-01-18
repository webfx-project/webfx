package naga.fx.spi.peer.base;

import emul.javafx.beans.property.Property;
import emul.javafx.scene.control.Slider;

/**
 * @author Bruno Salmon
 */
public interface SliderPeerMixin
        <N extends Slider, NB extends SliderPeerBase<N, NB, NM>, NM extends SliderPeerMixin<N, NB, NM>>

        extends ControlPeerMixin<N, NB, NM> {

    void updateMin(Double min);

    void updateMax(Double max);

    void updateValue(Double value);

    default void updateNodeValue(Double value) {
        Property<Double> valueProperty = getNodePeerBase().getNode().valueProperty();
        if (!valueProperty.isBound())
            valueProperty.setValue(value);
    }

}
