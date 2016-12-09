package naga.toolkit.fx.spi.view.base;

import naga.toolkit.fx.ext.DisplayResultSetControl;
import naga.toolkit.display.DisplayResultSet;
import naga.toolkit.fx.spi.view.DisplayResultSetControlView;

/**
 * @author Bruno Salmon
 */
public interface DisplayResultSetControlViewMixin
        <C, N extends DisplayResultSetControl, NV extends DisplayResultSetControlViewBase<C, N, NV, NM>, NM extends DisplayResultSetControlViewMixin<C, N, NV, NM>>

        extends DisplayResultSetControlView<N>,
        ControlViewMixin<N, NV, NM> {

    void updateResultSet(DisplayResultSet rs);

}
