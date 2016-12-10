package naga.toolkit.fx.spi.viewer.base;

import javafx.beans.property.Property;
import naga.toolkit.fx.scene.control.ButtonBase;
import naga.toolkit.fx.spi.DrawingRequester;
import naga.toolkit.fx.spi.viewer.ButtonBaseViewer;

/**
 * @author Bruno Salmon
 */
public abstract class ButtonBaseViewerBase
        <N extends ButtonBase, NV extends ButtonBaseViewerBase<N, NV, NM>, NM extends ButtonBaseViewerMixin<N, NV, NM>>

        extends LabeledViewerBase<N, NV, NM>
        implements ButtonBaseViewer<N> {

    @Override
    public void bind(N buttonBase, DrawingRequester drawingRequester) {
        super.bind(buttonBase, drawingRequester);
    }

    @Override
    public boolean updateProperty(Property changedProperty) {
        return super.updateProperty(changedProperty);
    }
}
