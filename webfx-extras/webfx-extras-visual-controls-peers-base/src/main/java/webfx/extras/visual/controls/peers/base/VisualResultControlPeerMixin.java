package webfx.extras.visual.controls.peers.base;

import webfx.extras.visual.controls.VisualResultControl;
import webfx.extras.visual.VisualResult;
import webfx.fxkit.javafxcontrols.mapper.spi.impl.peer.base.ControlPeerMixin;

/**
 * @author Bruno Salmon
 */
public interface VisualResultControlPeerMixin
        <C, N extends VisualResultControl, NB extends VisualResultControlPeerBase<C, N, NB, NM>, NM extends VisualResultControlPeerMixin<C, N, NB, NM>>

        extends ControlPeerMixin<N, NB, NM> {

    void updateVisualResult(VisualResult rs);

}
