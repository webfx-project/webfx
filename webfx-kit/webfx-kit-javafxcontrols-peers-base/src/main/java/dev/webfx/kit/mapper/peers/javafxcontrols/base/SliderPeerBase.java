package dev.webfx.kit.mapper.peers.javafxcontrols.base;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.Slider;
import dev.webfx.kit.mapper.peers.javafxgraphics.SceneRequester;

/**
 * @author Bruno Salmon
 */
public class SliderPeerBase
        <N extends Slider, NB extends SliderPeerBase<N, NB, NM>, NM extends SliderPeerMixin<N, NB, NM>>

        extends ControlPeerBase<N, NB, NM> {

    @Override
    public void bind(N s, SceneRequester sceneRequester) {
        super.bind(s, sceneRequester);
        requestUpdateOnPropertiesChange(sceneRequester
                , s.minProperty()
                , s.maxProperty()
                , s.valueProperty()
                , s.orientationProperty()
        );
    }

    @Override
    public boolean updateProperty(ObservableValue changedProperty) {
        N s = node;
        return super.updateProperty(changedProperty)
                || updateProperty(s.minProperty(), changedProperty, mixin::updateMin)
                || updateProperty(s.maxProperty(), changedProperty, mixin::updateMax)
                || updateProperty(s.valueProperty(), changedProperty, mixin::updateValue)
                || updateProperty(s.orientationProperty(), changedProperty, mixin::updateOrientation)
                ;
    }
}
