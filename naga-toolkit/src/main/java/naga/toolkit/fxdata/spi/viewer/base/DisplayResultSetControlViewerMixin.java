package naga.toolkit.fxdata.spi.viewer.base;

import naga.toolkit.fx.spi.viewer.base.ControlViewerMixin;
import naga.toolkit.fxdata.displaydata.DisplayResultSet;
import naga.toolkit.fxdata.DisplayResultSetControl;

/**
 * @author Bruno Salmon
 */
public interface DisplayResultSetControlViewerMixin
        <C, N extends DisplayResultSetControl, NB extends DisplayResultSetControlViewerBase<C, N, NB, NM>, NM extends DisplayResultSetControlViewerMixin<C, N, NB, NM>>

        extends ControlViewerMixin<N, NB, NM> {

    void updateResultSet(DisplayResultSet rs);

}
