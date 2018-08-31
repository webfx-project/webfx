package webfx.fx.spi.peer.base;

import emul.javafx.beans.value.ObservableValue;
import emul.javafx.scene.control.ScrollPane;
import webfx.fx.scene.SceneRequester;

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
