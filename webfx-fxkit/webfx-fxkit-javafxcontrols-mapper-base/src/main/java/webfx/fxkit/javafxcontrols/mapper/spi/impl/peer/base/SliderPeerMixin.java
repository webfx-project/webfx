package webfx.fxkit.javafxcontrols.mapper.spi.impl.peer.base;

import javafx.beans.property.DoubleProperty;
import javafx.scene.control.Slider;

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
