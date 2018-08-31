package webfx.fxdata.spi.peer.base;

import webfx.fx.spi.peer.base.ControlPeerMixin;
import webfx.fxdata.control.DisplayResultControl;
import webfx.fxdata.displaydata.DisplayResult;

/**
 * @author Bruno Salmon
 */
public interface DisplayResultControlPeerMixin
        <C, N extends DisplayResultControl, NB extends DisplayResultControlPeerBase<C, N, NB, NM>, NM extends DisplayResultControlPeerMixin<C, N, NB, NM>>

        extends ControlPeerMixin<N, NB, NM> {

    void updateResult(DisplayResult rs);

}
