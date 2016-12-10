package naga.toolkit.fx.spi.viewer.base;

import naga.toolkit.display.DisplaySelection;
import naga.toolkit.fx.ext.SelectableDisplayResultSetControl;
import naga.toolkit.fx.spi.viewer.SelectableDisplayResultSetControlViewer;
import naga.toolkit.properties.markers.SelectionMode;

/**
 * @author Bruno Salmon
 */
public interface SelectableDisplayResultSetControlViewerMixin
        <C, N extends SelectableDisplayResultSetControl, NV extends SelectableDisplayResultSetControlViewerBase<C, N, NV, NM>, NM extends SelectableDisplayResultSetControlViewerMixin<C, N, NV, NM>>

        extends SelectableDisplayResultSetControlViewer<N>,
        DisplayResultSetControlViewerMixin<C, N, NV, NM> {

    void updateSelectionMode(SelectionMode mode);

    void updateDisplaySelection(DisplaySelection selection);
}
