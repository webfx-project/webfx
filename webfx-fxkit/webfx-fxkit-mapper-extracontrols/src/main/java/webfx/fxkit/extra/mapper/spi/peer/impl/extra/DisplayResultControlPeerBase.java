package webfx.fxkit.extra.mapper.spi.peer.impl.extra;

import javafx.beans.value.ObservableValue;
import webfx.fxkit.extra.controls.displaydata.DisplayResultControl;
import webfx.fxkit.mapper.spi.SceneRequester;
import webfx.fxkit.mapper.spi.impl.peer.javafxcontrols.ControlPeerBase;

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
