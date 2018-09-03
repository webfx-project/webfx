package webfx.fxkits.extra.spi.peer.base;

import webfx.fxkits.extra.displaydata.DisplaySelection;
import webfx.fxkits.extra.control.SelectableDisplayResultControl;
import webfx.fxkits.extra.displaydata.SelectionMode;

/**
 * @author Bruno Salmon
 */
public interface SelectableDisplayResultControlPeerMixin
        <C, N extends SelectableDisplayResultControl, NB extends SelectableDisplayResultControlPeerBase<C, N, NB, NM>, NM extends SelectableDisplayResultControlPeerMixin<C, N, NB, NM>>

        extends DisplayResultControlPeerMixin<C, N, NB, NM> {

    void updateSelectionMode(SelectionMode mode);

    void updateDisplaySelection(DisplaySelection selection);
}
