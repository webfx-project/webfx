package naga.fxdata.spi.peer.base;

import javafx.beans.value.ObservableValue;
import naga.fx.spi.peer.base.ControlPeerBase;
import naga.fxdata.control.DisplayResultSetControl;
import naga.fx.scene.SceneRequester;

/**
 * @author Bruno Salmon
 */
public class DisplayResultSetControlPeerBase
        <C, N extends DisplayResultSetControl, NB extends DisplayResultSetControlPeerBase<C, N, NB, NM>, NM extends DisplayResultSetControlPeerMixin<C, N, NB, NM>>

        extends ControlPeerBase<N, NB, NM> {

    @Override
    public void bind(N shape, SceneRequester sceneRequester) {
        super.bind(shape, sceneRequester);
        requestUpdateOnPropertiesChange(sceneRequester
                , node.displayResultSetProperty()
        );
    }

    @Override
    public boolean updateProperty(ObservableValue changedProperty) {
        return super.updateProperty(changedProperty)
                || updateProperty(node.displayResultSetProperty(), changedProperty, mixin::updateResultSet)
                ;
    }

}
