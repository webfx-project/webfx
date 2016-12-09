package naga.toolkit.fx.spi.view.base;

import javafx.beans.property.Property;
import naga.toolkit.fx.ext.SelectableDisplayResultSetControl;
import naga.toolkit.fx.spi.DrawingRequester;
import naga.toolkit.fx.spi.view.SelectableDisplayResultSetControlView;

/**
 * @author Bruno Salmon
 */
public abstract class SelectableDisplayResultSetControlViewBase
        <C, N extends SelectableDisplayResultSetControl, NV extends SelectableDisplayResultSetControlViewBase<C, N, NV, NM>, NM extends SelectableDisplayResultSetControlViewMixin<C, N, NV, NM>>

        extends DisplayResultSetControlViewBase<C, N, NV, NM>
        implements SelectableDisplayResultSetControlView<N> {

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
