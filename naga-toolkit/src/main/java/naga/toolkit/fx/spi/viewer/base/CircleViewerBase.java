package naga.toolkit.fx.spi.viewer.base;

import javafx.beans.value.ObservableValue;
import naga.toolkit.fx.scene.SceneRequester;
import naga.toolkit.fx.scene.shape.Circle;

/**
 * @author Bruno Salmon
 */
public class CircleViewerBase
        <N extends Circle, NB extends CircleViewerBase<N, NB, NM>, NM extends CircleViewerMixin<N, NB, NM>>

        extends ShapeViewerBase<N, NB, NM> {

    @Override
    public void bind(N c, SceneRequester sceneRequester) {
        super.bind(c, sceneRequester);
        requestUpdateOnPropertiesChange(sceneRequester
                , c.centerXProperty()
                , c.centerYProperty()
                , c.radiusProperty()
        );
    }

    @Override
    public boolean updateProperty(ObservableValue changedProperty) {
        N c = node;
        return super.updateProperty(changedProperty)
                || updateProperty(c.centerXProperty(), changedProperty, mixin::updateCenterX)
                || updateProperty(c.centerYProperty(), changedProperty, mixin::updateCenterY)
                || updateProperty(c.radiusProperty(), changedProperty, mixin::updateRadius)
                ;
    }
}
