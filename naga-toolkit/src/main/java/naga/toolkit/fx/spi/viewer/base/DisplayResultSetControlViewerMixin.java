package naga.toolkit.fx.spi.viewer.base;

import naga.toolkit.display.DisplayResultSet;
import naga.toolkit.fx.ext.DisplayResultSetControl;

/**
 * @author Bruno Salmon
 */
public interface DisplayResultSetControlViewerMixin
        <C, N extends DisplayResultSetControl, NV extends DisplayResultSetControlViewerBase<C, N, NV, NM>, NM extends DisplayResultSetControlViewerMixin<C, N, NV, NM>>

        extends ControlViewerMixin<N, NV, NM> {

    void updateResultSet(DisplayResultSet rs);

}
