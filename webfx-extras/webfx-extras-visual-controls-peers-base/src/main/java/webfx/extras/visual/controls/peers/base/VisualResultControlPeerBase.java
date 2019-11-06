package webfx.extras.visual.controls.peers.base;

import javafx.beans.value.ObservableValue;
import webfx.extras.visual.controls.VisualResultControl;
import webfx.kit.mapper.peers.javafxgraphics.SceneRequester;
import webfx.kit.mapper.peers.javafxcontrols.base.ControlPeerBase;

/**
 * @author Bruno Salmon
 */
public class VisualResultControlPeerBase
        <C, N extends VisualResultControl, NB extends VisualResultControlPeerBase<C, N, NB, NM>, NM extends VisualResultControlPeerMixin<C, N, NB, NM>>

        extends ControlPeerBase<N, NB, NM> {

    @Override
    public void bind(N shape, SceneRequester sceneRequester) {
        super.bind(shape, sceneRequester);
        requestUpdateOnPropertiesChange(sceneRequester
                , node.visualResultProperty()
        );
    }

    @Override
    public boolean updateProperty(ObservableValue changedProperty) {
        return super.updateProperty(changedProperty)
                || updateProperty(node.visualResultProperty(), changedProperty, mixin::updateVisualResult)
                ;
    }

}
