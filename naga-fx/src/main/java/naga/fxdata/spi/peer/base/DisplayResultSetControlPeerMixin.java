package naga.fxdata.spi.peer.base;

import naga.fx.spi.peer.base.ControlPeerMixin;
import naga.fxdata.displaydata.DisplayResultSet;
import naga.fxdata.DisplayResultSetControl;

/**
 * @author Bruno Salmon
 */
public interface DisplayResultSetControlPeerMixin
        <C, N extends DisplayResultSetControl, NB extends DisplayResultSetControlPeerBase<C, N, NB, NM>, NM extends DisplayResultSetControlPeerMixin<C, N, NB, NM>>

        extends ControlPeerMixin<N, NB, NM> {

    void updateResultSet(DisplayResultSet rs);

}
