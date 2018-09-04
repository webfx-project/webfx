package webfx.fxkits.extra.spi.peer.base;

import webfx.fxkits.core.spi.peer.base.ControlPeerMixin;
import webfx.fxkits.extra.displaydata.DisplayResult;
import webfx.fxkits.extra.control.DisplayResultControl;

/**
 * @author Bruno Salmon
 */
public interface DisplayResultControlPeerMixin
        <C, N extends DisplayResultControl, NB extends DisplayResultControlPeerBase<C, N, NB, NM>, NM extends DisplayResultControlPeerMixin<C, N, NB, NM>>

        extends ControlPeerMixin<N, NB, NM> {

    void updateResult(DisplayResult rs);

}
