package webfx.fxkit.mapper.spi.impl.peer.javafxcontrols;

import javafx.scene.control.Control;
import webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.base.RegionPeerMixin;

/**
 * @author Bruno Salmon
 */
public interface ControlPeerMixin
        <N extends Control, NB extends ControlPeerBase<N, NB, NM>, NM extends ControlPeerMixin<N, NB, NM>>

        extends RegionPeerMixin<N, NB, NM> {
}
