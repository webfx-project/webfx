package webfx.fxkits.extra.mapper.spi.peer.impl;

import javafx.beans.value.ObservableValue;
import webfx.fxkits.extra.control.SelectableDisplayResultControl;
import webfx.fxkits.core.mapper.spi.SceneRequester;

/**
 * @author Bruno Salmon
 */
public abstract class SelectableDisplayResultControlPeerBase
        <C, N extends SelectableDisplayResultControl, NB extends SelectableDisplayResultControlPeerBase<C, N, NB, NM>, NM extends SelectableDisplayResultControlPeerMixin<C, N, NB, NM>>

        extends DisplayResultControlPeerBase<C, N, NB, NM> {

    @Override
    public void bind(N shape, SceneRequester sceneRequester) {
        super.bind(shape, sceneRequester);
        requestUpdateOnPropertiesChange(sceneRequester
                , node.selectionModeProperty()
                , node.displaySelectionProperty()
        );
    }

    @Override
    public boolean updateProperty(ObservableValue changedProperty) {
        return super.updateProperty(changedProperty)
                || updateProperty(node.selectionModeProperty(), changedProperty, mixin::updateSelectionMode)
                || updateProperty(node.displaySelectionProperty(), changedProperty, mixin::updateDisplaySelection)
                ;
    }
}
