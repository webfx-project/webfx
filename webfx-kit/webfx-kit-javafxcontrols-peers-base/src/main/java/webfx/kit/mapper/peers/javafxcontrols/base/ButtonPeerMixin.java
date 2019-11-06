package webfx.kit.mapper.peers.javafxcontrols.base;

import javafx.scene.control.Button;

/**
 * @author Bruno Salmon
 */
public interface ButtonPeerMixin
        <N extends Button, NB extends ButtonPeerBase<N, NB, NM>, NM extends ButtonPeerMixin<N, NB, NM>>

        extends ButtonBasePeerMixin<N, NB, NM> {
}
