package webfx.fxkit.extra.mapper.spi.peer.impl;

import webfx.fxkit.mapper.spi.impl.peer.ControlPeerMixin;
import webfx.fxkit.extra.displaydata.DisplayResult;
import webfx.fxkit.extra.control.DisplayResultControl;

/**
 * @author Bruno Salmon
 */
public interface DisplayResultControlPeerMixin
        <C, N extends DisplayResultControl, NB extends DisplayResultControlPeerBase<C, N, NB, NM>, NM extends DisplayResultControlPeerMixin<C, N, NB, NM>>

        extends ControlPeerMixin<N, NB, NM> {

    void updateResult(DisplayResult rs);

}
