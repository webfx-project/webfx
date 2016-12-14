package naga.toolkit.fx.spi.viewer.base;

import javafx.beans.value.ObservableValue;
import naga.toolkit.fx.ext.DisplayResultSetControl;
import naga.toolkit.fx.spi.DrawingRequester;
import naga.toolkit.fx.spi.viewer.DisplayResultSetControlViewer;

/**
 * @author Bruno Salmon
 */
public class DisplayResultSetControlViewerBase
        <C, N extends DisplayResultSetControl, NV extends DisplayResultSetControlViewerBase<C, N, NV, NM>, NM extends DisplayResultSetControlViewerMixin<C, N, NV, NM>>

        extends ControlViewerBase<N, NV, NM>
        implements DisplayResultSetControlViewer<N> {

    @Override
    public void bind(N shape, DrawingRequester drawingRequester) {
        super.bind(shape, drawingRequester);
        requestUpdateOnPropertiesChange(drawingRequester
                , node.displayResultSetProperty()
        );
    }

    @Override
    public boolean updateProperty(ObservableValue changedProperty) {
        return super.updateProperty(changedProperty)
                || updateProperty(node.displayResultSetProperty(), changedProperty, mixin::updateResultSet)
                ;
    }

}
