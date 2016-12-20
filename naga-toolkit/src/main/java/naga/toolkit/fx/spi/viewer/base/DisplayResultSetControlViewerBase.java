package naga.toolkit.fx.spi.viewer.base;

import javafx.beans.value.ObservableValue;
import naga.toolkit.fx.ext.DisplayResultSetControl;
import naga.toolkit.fx.scene.SceneRequester;

/**
 * @author Bruno Salmon
 */
public class DisplayResultSetControlViewerBase
        <C, N extends DisplayResultSetControl, NV extends DisplayResultSetControlViewerBase<C, N, NV, NM>, NM extends DisplayResultSetControlViewerMixin<C, N, NV, NM>>

        extends ControlViewerBase<N, NV, NM> {

    @Override
    public void bind(N shape, SceneRequester sceneRequester) {
        super.bind(shape, sceneRequester);
        requestUpdateOnPropertiesChange(sceneRequester
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
