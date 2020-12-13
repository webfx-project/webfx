package dev.webfx.kit.mapper.peers.javafxcontrols.base;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.ScrollPane;
import dev.webfx.kit.mapper.peers.javafxgraphics.SceneRequester;
import dev.webfx.kit.mapper.peers.javafxgraphics.base.RegionPeerBase;

/**
 * @author Bruno Salmon
 */
public class ScrollPanePeerBase
        <N extends ScrollPane, NB extends ScrollPanePeerBase<N, NB, NM>, NM extends ScrollPanePeerMixin<N, NB, NM>>

        extends RegionPeerBase<N, NB, NM> {

    @Override
    public void bind(N node, SceneRequester sceneRequester) {
        super.bind(node, sceneRequester);
        requestUpdateOnPropertiesChange(sceneRequester
                , node.hbarPolicyProperty()
                , node.vbarPolicyProperty()
                , node.hvalueProperty()
                , node.vvalueProperty()
                , node.hminProperty()
                , node.hmaxProperty()
                , node.vminProperty()
                , node.vmaxProperty()
        );
    }

    @Override
    public boolean updateProperty(ObservableValue changedProperty) {
        return super.updateProperty(changedProperty)
                || updateProperty(node.hbarPolicyProperty(), changedProperty, mixin::updateHbarPolicy)
                || updateProperty(node.vbarPolicyProperty(), changedProperty, mixin::updateVbarPolicy)
                || updateProperty(node.hvalueProperty(), changedProperty, mixin::updateHvalue)
                || updateProperty(node.vvalueProperty(), changedProperty, mixin::updateVvalue)
                || updateProperty(node.hminProperty(), changedProperty, mixin::updateHmin)
                || updateProperty(node.hmaxProperty(), changedProperty, mixin::updateHmax)
                || updateProperty(node.vminProperty(), changedProperty, mixin::updateVmin)
                || updateProperty(node.vmaxProperty(), changedProperty, mixin::updateVmax)
                ;
    }

}
