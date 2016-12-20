package naga.toolkit.fx.spi.viewer.base;

import javafx.beans.value.ObservableValue;
import naga.toolkit.fx.scene.SceneRequester;
import naga.toolkit.fx.scene.control.Slider;

/**
 * @author Bruno Salmon
 */
public class SliderViewerBase
        <N extends Slider, NV extends SliderViewerBase<N, NV, NM>, NM extends SliderViewerMixin<N, NV, NM>>

        extends ControlViewerBase<N, NV, NM> {

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
