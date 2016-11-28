package naga.toolkit.drawing.spi.view.base;

import javafx.beans.property.Property;
import naga.toolkit.drawing.scene.control.Control;
import naga.toolkit.drawing.spi.DrawingRequester;
import naga.toolkit.drawing.spi.view.ControlView;

/**
 * @author Bruno Salmon
 */
public abstract class ControlViewBase
        <N extends Control, NV extends ControlViewBase<N, NV, NM>, NM extends ControlViewMixin<N, NV, NM>>

        extends NodeViewBase<N, NV, NM>
        implements ControlView<N> {

    @Override
    public void bind(N shape, DrawingRequester drawingRequester) {
        super.bind(shape, drawingRequester);
    }

    @Override
    public boolean updateProperty(Property changedProperty) {
        return super.updateProperty(changedProperty);
    }
}
