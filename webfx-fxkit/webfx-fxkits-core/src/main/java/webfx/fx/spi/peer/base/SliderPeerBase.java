package webfx.fx.spi.peer.base;

import javafx.beans.value.ObservableValue;
import webfx.fx.scene.SceneRequester;
import javafx.scene.control.Slider;

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
                || updateProperty(s.minProperty(), changedProperty, p -> mixin.updateMin(p.doubleValue()))
                || updateProperty(s.maxProperty(), changedProperty, p -> mixin.updateMax(p.doubleValue()))
                || updateProperty(s.valueProperty(), changedProperty, p -> mixin.updateValue(p.doubleValue()))
                ;
    }
}
