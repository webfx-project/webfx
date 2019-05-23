package webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.base;

import javafx.beans.value.ObservableValue;
import webfx.fxkit.javafxgraphics.mapper.spi.SceneRequester;
import javafx.scene.shape.Circle;

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
                || updateProperty(c.centerXProperty(), changedProperty, p -> mixin.updateCenterX(p.doubleValue()))
                || updateProperty(c.centerYProperty(), changedProperty, p -> mixin.updateCenterY(p.doubleValue()))
                || updateProperty(c.radiusProperty(), changedProperty, p-> mixin.updateRadius(p.doubleValue()))
                ;
    }
}
