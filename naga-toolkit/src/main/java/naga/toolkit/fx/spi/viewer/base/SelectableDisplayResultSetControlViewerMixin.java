package naga.toolkit.fx.spi.viewer.base;

import naga.toolkit.display.DisplaySelection;
import naga.toolkit.fx.ext.SelectableDisplayResultSetControl;
import naga.toolkit.properties.markers.SelectionMode;

/**
 * @author Bruno Salmon
 */
public interface SelectableDisplayResultSetControlViewerMixin
        <C, N extends SelectableDisplayResultSetControl, NB extends SelectableDisplayResultSetControlViewerBase<C, N, NB, NM>, NM extends SelectableDisplayResultSetControlViewerMixin<C, N, NB, NM>>

        extends DisplayResultSetControlViewerMixin<C, N, NB, NM> {

    void updateSelectionMode(SelectionMode mode);

    void updateDisplaySelection(DisplaySelection selection);
}
