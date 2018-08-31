package webfx.fxdata.spi.peer.base;

import webfx.fxdata.control.SelectableDisplayResultControl;
import webfx.fxdata.displaydata.DisplaySelection;
import webfx.fxdata.displaydata.SelectionMode;

/**
 * @author Bruno Salmon
 */
public interface SelectableDisplayResultControlPeerMixin
        <C, N extends SelectableDisplayResultControl, NB extends SelectableDisplayResultControlPeerBase<C, N, NB, NM>, NM extends SelectableDisplayResultControlPeerMixin<C, N, NB, NM>>

        extends DisplayResultControlPeerMixin<C, N, NB, NM> {

    void updateSelectionMode(SelectionMode mode);

    void updateDisplaySelection(DisplaySelection selection);
}
