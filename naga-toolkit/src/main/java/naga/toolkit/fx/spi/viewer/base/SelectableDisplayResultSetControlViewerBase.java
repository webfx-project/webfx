package naga.toolkit.fx.spi.viewer.base;

import javafx.beans.value.ObservableValue;
import naga.toolkit.fx.ext.SelectableDisplayResultSetControl;
import naga.toolkit.fx.scene.SceneRequester;
import naga.toolkit.fx.spi.viewer.SelectableDisplayResultSetControlViewer;

/**
 * @author Bruno Salmon
 */
public abstract class SelectableDisplayResultSetControlViewerBase
        <C, N extends SelectableDisplayResultSetControl, NV extends SelectableDisplayResultSetControlViewerBase<C, N, NV, NM>, NM extends SelectableDisplayResultSetControlViewerMixin<C, N, NV, NM>>

        extends DisplayResultSetControlViewerBase<C, N, NV, NM>
        implements SelectableDisplayResultSetControlViewer<N> {

    @Override
    public void bind(N shape, SceneRequester sceneRequester) {
        super.bind(shape, sceneRequester);
        requestUpdateOnPropertiesChange(sceneRequester
                , node.selectionModeProperty()
                , node.displaySelectionProperty()
        );
    }

    @Override
    public boolean updateProperty(ObservableValue changedProperty) {
        return super.updateProperty(changedProperty)
                || updateProperty(node.selectionModeProperty(), changedProperty, mixin::updateSelectionMode)
                || updateProperty(node.displaySelectionProperty(), changedProperty, mixin::updateDisplaySelection)
                ;
    }
}
