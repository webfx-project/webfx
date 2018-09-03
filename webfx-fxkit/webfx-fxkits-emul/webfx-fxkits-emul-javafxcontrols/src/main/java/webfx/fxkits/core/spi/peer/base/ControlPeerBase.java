package webfx.fxkits.core.spi.peer.base;

import emul.javafx.scene.control.Control;
import webfx.fxkits.core.scene.SceneRequester;

/**
 * @author Bruno Salmon
 */
public abstract class ControlPeerBase
        <N extends Control, NB extends ControlPeerBase<N, NB, NM>, NM extends ControlPeerMixin<N, NB, NM>>

        extends RegionPeerBase<N, NB, NM> {

    @Override
    public void bind(N node, SceneRequester sceneRequester) {
        node.getProperties().put("skinProperty", node.skinProperty()); // used by Scene emulation
        super.bind(node, sceneRequester);
    }
}
