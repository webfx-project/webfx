package naga.toolkit.fx.spi.viewer.base;

import javafx.beans.property.Property;
import naga.toolkit.fx.ext.SelectableDisplayResultSetControl;
import naga.toolkit.fx.spi.DrawingRequester;
import naga.toolkit.fx.spi.viewer.SelectableDisplayResultSetControlViewer;

/**
 * @author Bruno Salmon
 */
public abstract class SelectableDisplayResultSetControlViewerBase
        <C, N extends SelectableDisplayResultSetControl, NV extends SelectableDisplayResultSetControlViewerBase<C, N, NV, NM>, NM extends SelectableDisplayResultSetControlViewerMixin<C, N, NV, NM>>

        extends DisplayResultSetControlViewerBase<C, N, NV, NM>
        implements SelectableDisplayResultSetControlViewer<N> {

    @Override
    public void bind(N shape, DrawingRequester drawingRequester) {
        super.bind(shape, drawingRequester);
        requestUpdateOnPropertiesChange(drawingRequester,
                node.selectionModeProperty(),
                node.displaySelectionProperty());
    }

    @Override
    public boolean updateProperty(Property changedProperty) {
        return super.updateProperty(changedProperty)
                || updateProperty(node.selectionModeProperty(), changedProperty, mixin::updateSelectionMode)
                || updateProperty(node.displaySelectionProperty(), changedProperty, mixin::updateDisplaySelection)
                ;
    }
}
