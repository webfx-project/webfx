package naga.fxdata.spi.peer.base;

import naga.fxdata.displaydata.DisplaySelection;
import naga.fxdata.SelectableDisplayResultSetControl;
import naga.fxdata.displaydata.SelectionMode;

/**
 * @author Bruno Salmon
 */
public interface SelectableDisplayResultSetControlPeerMixin
        <C, N extends SelectableDisplayResultSetControl, NB extends SelectableDisplayResultSetControlPeerBase<C, N, NB, NM>, NM extends SelectableDisplayResultSetControlPeerMixin<C, N, NB, NM>>

        extends DisplayResultSetControlPeerMixin<C, N, NB, NM> {

    void updateSelectionMode(SelectionMode mode);

    void updateDisplaySelection(DisplaySelection selection);
}
