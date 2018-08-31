package webfx.fx.spi.peer.base;

import javafx.beans.value.ObservableValue;
import webfx.fx.scene.SceneRequester;
import javafx.scene.control.ToggleButton;

/**
 * @author Bruno Salmon
 */
public class ToggleButtonPeerBase
        <N extends ToggleButton, NB extends ToggleButtonPeerBase<N, NB, NM>, NM extends ToggleButtonPeerMixin<N, NB, NM>>
        extends ButtonBasePeerBase<N, NB, NM> {

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
