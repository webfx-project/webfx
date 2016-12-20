package naga.toolkit.fx.spi.viewer.base;

import javafx.beans.value.ObservableValue;
import naga.toolkit.fx.scene.SceneRequester;
import naga.toolkit.fx.scene.control.ToggleButton;

/**
 * @author Bruno Salmon
 */
public class ToggleButtonViewerBase
        <N extends ToggleButton, NB extends ToggleButtonViewerBase<N, NB, NM>, NM extends ToggleButtonViewerMixin<N, NB, NM>>
        extends ButtonBaseViewerBase<N, NB, NM> {

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
