package naga.fx.spi.viewer.base;

import javafx.beans.value.ObservableValue;
import naga.fx.scene.SceneRequester;
import naga.fx.scene.control.Slider;

/**
 * @author Bruno Salmon
 */
public class SliderViewerBase
        <N extends Slider, NB extends SliderViewerBase<N, NB, NM>, NM extends SliderViewerMixin<N, NB, NM>>

        extends ControlViewerBase<N, NB, NM> {

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
