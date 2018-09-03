package webfx.fxkits.extra.spi.peer.base;

import emul.javafx.beans.value.ObservableValue;
import webfx.fxkits.core.scene.SceneRequester;
import webfx.fxkits.core.spi.peer.base.ControlPeerBase;
import webfx.fxkits.extra.control.DisplayResultControl;

/**
 * @author Bruno Salmon
 */
public class DisplayResultControlPeerBase
        <C, N extends DisplayResultControl, NB extends DisplayResultControlPeerBase<C, N, NB, NM>, NM extends DisplayResultControlPeerMixin<C, N, NB, NM>>

        extends ControlPeerBase<N, NB, NM> {

    @Override
    public void bind(N shape, SceneRequester sceneRequester) {
        super.bind(shape, sceneRequester);
        requestUpdateOnPropertiesChange(sceneRequester
                , node.displayResultProperty()
        );
    }

    @Override
    public boolean updateProperty(ObservableValue changedProperty) {
        return super.updateProperty(changedProperty)
                || updateProperty(node.displayResultProperty(), changedProperty, mixin::updateResult)
                ;
    }

}
