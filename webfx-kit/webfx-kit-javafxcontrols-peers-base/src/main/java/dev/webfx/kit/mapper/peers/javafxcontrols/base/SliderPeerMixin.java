package dev.webfx.kit.mapper.peers.javafxcontrols.base;

import javafx.beans.property.DoubleProperty;
import javafx.geometry.Orientation;
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

    void updateOrientation(Orientation orientation);

    default void updateNodeValue(Double value) {
        DoubleProperty valueProperty = getNodePeerBase().getNode().valueProperty();
        if (!valueProperty.isBound())
            valueProperty.setValue(value);
    }

}
