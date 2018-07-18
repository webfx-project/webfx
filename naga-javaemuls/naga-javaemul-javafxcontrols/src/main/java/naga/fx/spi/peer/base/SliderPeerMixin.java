package naga.fx.spi.peer.base;

import emul.javafx.beans.property.DoubleProperty;
import emul.javafx.scene.control.Slider;

/**
 * @author Bruno Salmon
 */
public interface SliderPeerMixin
        <N extends Slider, NB extends SliderPeerBase<N, NB, NM>, NM extends SliderPeerMixin<N, NB, NM>>

        extends ControlPeerMixin<N, NB, NM> {

    void updateMin(Number min);

    void updateMax(Number max);

    void updateValue(Number value);

    default void updateNodeValue(Double value) {
        DoubleProperty valueProperty = getNodePeerBase().getNode().valueProperty();
        if (!valueProperty.isBound())
            valueProperty.setValue(value);
    }

}
