package naga.toolkit.fx.spi.view.base;

import javafx.beans.property.Property;
import naga.toolkit.fx.ext.DisplayResultSetControl;
import naga.toolkit.fx.spi.DrawingRequester;
import naga.toolkit.fx.spi.view.DisplayResultSetControlView;

/**
 * @author Bruno Salmon
 */
public class DisplayResultSetControlViewBase
        <C, N extends DisplayResultSetControl, NV extends DisplayResultSetControlViewBase<C, N, NV, NM>, NM extends DisplayResultSetControlViewMixin<C, N, NV, NM>>

        extends ControlViewBase<N, NV, NM>
        implements DisplayResultSetControlView<N> {

    @Override
    public void bind(N shape, DrawingRequester drawingRequester) {
        super.bind(shape, drawingRequester);
        requestUpdateOnPropertiesChange(drawingRequester,
                node.displayResultSetProperty());
    }

    @Override
    public boolean updateProperty(Property changedProperty) {
        return super.updateProperty(changedProperty)
                || updateProperty(node.displayResultSetProperty(), changedProperty, mixin::updateResultSet)
                ;
    }

}
