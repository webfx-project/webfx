package naga.toolkit.fx.spi.viewer.base;

import javafx.beans.value.ObservableValue;
import naga.toolkit.fx.scene.SceneRequester;
import naga.toolkit.fx.scene.control.ToggleButton;

/**
 * @author Bruno Salmon
 */
public class ToggleButtonViewerBase
        <N extends ToggleButton, NV extends ToggleButtonViewerBase<N, NV, NM>, NM extends ToggleButtonViewerMixin<N, NV, NM>>
        extends ButtonBaseViewerBase<N, NV, NM> {

    @Override
    public void bind(N toggleButton, SceneRequester sceneRequester) {
        super.bind(toggleButton, sceneRequester);
        requestUpdateOnPropertiesChange(sceneRequester
                , node.selectedProperty()
        );
    }

    @Override
    public boolean updateProperty(ObservableValue changedProperty) {
        N tb = getNode();
        return super.updateProperty(changedProperty)
                || updateProperty(tb.selectedProperty(), changedProperty, mixin::updateSelected)
                ;
    }
}
