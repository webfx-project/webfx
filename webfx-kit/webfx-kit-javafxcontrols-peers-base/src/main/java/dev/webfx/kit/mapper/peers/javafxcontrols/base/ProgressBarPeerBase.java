package dev.webfx.kit.mapper.peers.javafxcontrols.base;

import javafx.scene.control.ProgressBar;

/**
 * @author Bruno Salmon
 */
public class ProgressBarPeerBase
        <N extends ProgressBar, NB extends ProgressBarPeerBase<N, NB, NM>, NM extends ProgressBarPeerMixin<N, NB, NM>>

        extends ProgressIndicatorPeerBase<N, NB, NM> {

}
