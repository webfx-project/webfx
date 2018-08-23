package naga.fx.spi.peer.base;

import javafx.scene.control.Control;
import naga.fx.scene.SceneRequester;

/**
 * @author Bruno Salmon
 */
public abstract class ControlPeerBase
        <N extends Control, NB extends ControlPeerBase<N, NB, NM>, NM extends ControlPeerMixin<N, NB, NM>>

        extends RegionPeerBase<N, NB, NM> {

    @Override
    public void bind(N node, SceneRequester sceneRequester) {
        super.bind(node, sceneRequester);
        if (node.getSkin() == null)
            node.getProperties().put("keepProperty", node.skinProperty());
    }
}
