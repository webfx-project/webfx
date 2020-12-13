package dev.webfx.kit.mapper.peers.javafxcontrols.base;

import javafx.scene.control.Control;
import dev.webfx.kit.mapper.peers.javafxgraphics.SceneRequester;
import dev.webfx.kit.mapper.peers.javafxgraphics.base.RegionPeerBase;

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
