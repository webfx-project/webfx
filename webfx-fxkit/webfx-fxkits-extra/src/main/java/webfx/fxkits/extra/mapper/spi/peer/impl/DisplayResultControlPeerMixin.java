package webfx.fxkits.extra.mapper.spi.peer.impl;

import webfx.fxkits.core.mapper.spi.impl.peer.ControlPeerMixin;
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
