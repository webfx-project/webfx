package naga.toolkit.fx.spi.viewer.base;

import naga.toolkit.display.DisplayResultSet;
import naga.toolkit.fx.ext.DisplayResultSetControl;

/**
 * @author Bruno Salmon
 */
public interface DisplayResultSetControlViewerMixin
        <C, N extends DisplayResultSetControl, NB extends DisplayResultSetControlViewerBase<C, N, NB, NM>, NM extends DisplayResultSetControlViewerMixin<C, N, NB, NM>>

        extends ControlViewerMixin<N, NB, NM> {

    void updateResultSet(DisplayResultSet rs);

}
