package webfx.fxkit.javafxcontrols.mapper.spi.impl.peer.base;

import javafx.scene.control.Control;
import webfx.fxkit.javafxgraphics.mapper.spi.SceneRequester;
import webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.base.RegionPeerBase;

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
