package naga.toolkit.fx.spi.view.base;

import naga.toolkit.display.DisplaySelection;
import naga.toolkit.fx.ext.SelectableDisplayResultSetControl;
import naga.toolkit.fx.spi.view.SelectableDisplayResultSetControlView;
import naga.toolkit.properties.markers.SelectionMode;

/**
 * @author Bruno Salmon
 */
public interface SelectableDisplayResultSetControlViewMixin
        <C, N extends SelectableDisplayResultSetControl, NV extends SelectableDisplayResultSetControlViewBase<C, N, NV, NM>, NM extends SelectableDisplayResultSetControlViewMixin<C, N, NV, NM>>

        extends SelectableDisplayResultSetControlView<N>,
        DisplayResultSetControlViewMixin<C, N, NV, NM> {

    void updateSelectionMode(SelectionMode mode);

    void updateDisplaySelection(DisplaySelection selection);
}
