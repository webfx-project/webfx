package naga.fx.spi.peer.base;

import emul.javafx.beans.value.ObservableValue;
import naga.fx.scene.SceneRequester;
import emul.javafx.scene.shape.Circle;

/**
 * @author Bruno Salmon
 */
public class CirclePeerBase
        <N extends Circle, NB extends CirclePeerBase<N, NB, NM>, NM extends CirclePeerMixin<N, NB, NM>>

        extends ShapePeerBase<N, NB, NM> {

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
