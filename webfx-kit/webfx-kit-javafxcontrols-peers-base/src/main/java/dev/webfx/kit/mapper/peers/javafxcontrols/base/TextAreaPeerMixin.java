package dev.webfx.kit.mapper.peers.javafxcontrols.base;

import javafx.scene.control.TextArea;

/**
 * @author Bruno Salmon
 */
public interface TextAreaPeerMixin
        <N extends TextArea, NB extends TextAreaPeerBase<N, NB, NM>, NM extends TextAreaPeerMixin<N, NB, NM>>

        extends TextInputControlPeerMixin<N, NB, NM> {
}
