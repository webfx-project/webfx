package webfx.fx.spi.peer.base;

import emul.javafx.beans.value.ObservableValue;
import webfx.fx.scene.SceneRequester;
import emul.javafx.scene.control.Slider;

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
        );
    }

    @Override
    public boolean updateProperty(ObservableValue changedProperty) {
        N s = node;
        return super.updateProperty(changedProperty)
                || updateProperty(s.minProperty(), changedProperty, mixin::updateMin)
                || updateProperty(s.maxProperty(), changedProperty, mixin::updateMax)
                || updateProperty(s.valueProperty(), changedProperty, mixin::updateValue)
                ;
    }
}
