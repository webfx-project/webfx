package webfx.fxkit.extra.controls.mapper.spi.peer.impl.base;

import javafx.beans.value.ObservableValue;
import webfx.fxkit.extra.controls.displaydata.DisplayResultControl;
import webfx.fxkit.javafxgraphics.mapper.spi.SceneRequester;
import webfx.fxkit.javafxcontrols.mapper.spi.impl.peer.base.ControlPeerBase;

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
