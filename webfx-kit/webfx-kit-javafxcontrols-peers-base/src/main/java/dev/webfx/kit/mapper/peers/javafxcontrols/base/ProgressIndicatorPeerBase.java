package dev.webfx.kit.mapper.peers.javafxcontrols.base;

import dev.webfx.kit.mapper.peers.javafxgraphics.SceneRequester;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ProgressIndicator;

/**
 * @author Bruno Salmon
 */
public abstract class ProgressIndicatorPeerBase
        <N extends ProgressIndicator, NB extends ProgressIndicatorPeerBase<N, NB, NM>, NM extends ProgressIndicatorPeerMixin<N, NB, NM>>

        extends ControlPeerBase<N, NB, NM> {

    @Override
    public void bind(N labeled, SceneRequester sceneRequester) {
        super.bind(labeled, sceneRequester);
        requestUpdateOnPropertiesChange(sceneRequester
                , node.progressProperty()
        );
    }

    @Override
    public boolean updateProperty(ObservableValue changedProperty) {
        return super.updateProperty(changedProperty)
                || updateProperty(node.progressProperty(), changedProperty, mixin::updateProgress)
                ;
    }
}
