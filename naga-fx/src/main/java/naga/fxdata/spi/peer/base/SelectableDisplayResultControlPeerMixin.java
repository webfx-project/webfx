package naga.fxdata.spi.peer.base;

import naga.fxdata.displaydata.DisplaySelection;
import naga.fxdata.control.SelectableDisplayResultControl;
import naga.fxdata.displaydata.SelectionMode;

/**
 * @author Bruno Salmon
 */
public interface SelectableDisplayResultControlPeerMixin
        <C, N extends SelectableDisplayResultControl, NB extends SelectableDisplayResultControlPeerBase<C, N, NB, NM>, NM extends SelectableDisplayResultControlPeerMixin<C, N, NB, NM>>

        extends DisplayResultControlPeerMixin<C, N, NB, NM> {

    void updateSelectionMode(SelectionMode mode);

    void updateDisplaySelection(DisplaySelection selection);
}
