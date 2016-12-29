package naga.fxdata.spi.viewer.base;

import javafx.beans.value.ObservableValue;
import naga.fx.spi.viewer.base.ControlViewerBase;
import naga.fxdata.DisplayResultSetControl;
import naga.fx.scene.SceneRequester;

/**
 * @author Bruno Salmon
 */
public class DisplayResultSetControlViewerBase
        <C, N extends DisplayResultSetControl, NB extends DisplayResultSetControlViewerBase<C, N, NB, NM>, NM extends DisplayResultSetControlViewerMixin<C, N, NB, NM>>

        extends ControlViewerBase<N, NB, NM> {

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
