package webfx.extras.visual.controls.peers.base;

import javafx.beans.value.ObservableValue;
import webfx.extras.visual.controls.SelectableVisualResultControl;
import webfx.kit.mapper.peers.javafxgraphics.SceneRequester;

/**
 * @author Bruno Salmon
 */
public abstract class SelectableVisualResultControlPeerBase
        <C, N extends SelectableVisualResultControl, NB extends SelectableVisualResultControlPeerBase<C, N, NB, NM>, NM extends SelectableVisualResultControlPeerMixin<C, N, NB, NM>>

        extends VisualResultControlPeerBase<C, N, NB, NM> {

    @Override
    public void bind(N shape, SceneRequester sceneRequester) {
        super.bind(shape, sceneRequester);
        requestUpdateOnPropertiesChange(sceneRequester
                , node.selectionModeProperty()
                , node.visualSelectionProperty()
        );
    }

    @Override
    public boolean updateProperty(ObservableValue changedProperty) {
        return super.updateProperty(changedProperty)
                || updateProperty(node.selectionModeProperty(), changedProperty, mixin::updateSelectionMode)
                || updateProperty(node.visualSelectionProperty(), changedProperty, mixin::updateVisualSelection)
                ;
    }
}
