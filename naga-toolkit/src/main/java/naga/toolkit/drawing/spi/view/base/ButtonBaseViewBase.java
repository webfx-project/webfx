package naga.toolkit.drawing.spi.view.base;

import javafx.beans.property.Property;
import naga.toolkit.drawing.scene.control.ButtonBase;
import naga.toolkit.drawing.spi.DrawingRequester;
import naga.toolkit.drawing.spi.view.ButtonBaseView;

/**
 * @author Bruno Salmon
 */
public abstract class ButtonBaseViewBase
        <N extends ButtonBase, NV extends ButtonBaseViewBase<N, NV, NM>, NM extends ButtonBaseViewMixin<N, NV, NM>>

        extends LabeledViewBase<N, NV, NM>
        implements ButtonBaseView<N> {

    @Override
    public void bind(N buttonBase, DrawingRequester drawingRequester) {
        super.bind(buttonBase, drawingRequester);
    }

    @Override
    public boolean updateProperty(Property changedProperty) {
        return super.updateProperty(changedProperty);
    }
}
