package dev.webfx.kit.mapper.peers.javafxcontrols.base;

import javafx.scene.control.Control;
import dev.webfx.kit.mapper.peers.javafxgraphics.base.RegionPeerMixin;

/**
 * @author Bruno Salmon
 */
public interface ControlPeerMixin
        <N extends Control, NB extends ControlPeerBase<N, NB, NM>, NM extends ControlPeerMixin<N, NB, NM>>

        extends RegionPeerMixin<N, NB, NM> {
}
