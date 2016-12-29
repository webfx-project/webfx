package naga.fxdata.spi.viewer.base;

import naga.fxdata.displaydata.DisplaySelection;
import naga.fxdata.SelectableDisplayResultSetControl;
import naga.fxdata.displaydata.SelectionMode;

/**
 * @author Bruno Salmon
 */
public interface SelectableDisplayResultSetControlViewerMixin
        <C, N extends SelectableDisplayResultSetControl, NB extends SelectableDisplayResultSetControlViewerBase<C, N, NB, NM>, NM extends SelectableDisplayResultSetControlViewerMixin<C, N, NB, NM>>

        extends DisplayResultSetControlViewerMixin<C, N, NB, NM> {

    void updateSelectionMode(SelectionMode mode);

    void updateDisplaySelection(DisplaySelection selection);
}
