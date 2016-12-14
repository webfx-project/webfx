package naga.toolkit.fx.spi.viewer.base;

import javafx.beans.value.ObservableValue;
import naga.toolkit.fx.scene.control.ToggleButton;
import naga.toolkit.fx.spi.DrawingRequester;
import naga.toolkit.fx.spi.viewer.ToggleButtonViewer;

/**
 * @author Bruno Salmon
 */
public class ToggleButtonViewerBase
        <N extends ToggleButton, NV extends ToggleButtonViewerBase<N, NV, NM>, NM extends ToggleButtonViewerMixin<N, NV, NM>>
        extends ButtonBaseViewerBase<N, NV, NM>
        implements ToggleButtonViewer<N> {

    @Override
    public void bind(N toggleButton, DrawingRequester drawingRequester) {
        super.bind(toggleButton, drawingRequester);
        requestUpdateOnPropertiesChange(drawingRequester
                , node.selectedProperty()
        );
    }

    @Override
    public boolean updateProperty(ObservableValue changedProperty) {
        N tb = getNode();
        return super.updateProperty(changedProperty)
                || updateProperty(tb.selectedProperty(), changedProperty, mixin::updateSelected)
                ;
    }
}
