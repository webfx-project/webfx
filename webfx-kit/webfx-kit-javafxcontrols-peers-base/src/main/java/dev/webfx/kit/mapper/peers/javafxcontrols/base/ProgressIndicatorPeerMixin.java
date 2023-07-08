package dev.webfx.kit.mapper.peers.javafxcontrols.base;

import javafx.scene.control.ProgressIndicator;

/**
 * @author Bruno Salmon
 */
public interface ProgressIndicatorPeerMixin
        <N extends ProgressIndicator, NB extends ProgressIndicatorPeerBase<N, NB, NM>, NM extends ProgressIndicatorPeerMixin<N, NB, NM>>

        extends ControlPeerMixin<N, NB, NM> {

    void updateProgress(Number progress);

}
