package webfx.kit.mapper.peers.javafxgraphics.base;

import javafx.beans.value.ObservableValue;
import javafx.scene.shape.Arc;
import webfx.kit.mapper.peers.javafxgraphics.SceneRequester;

/**
 * @author Bruno Salmon
 */
public class ArcPeerBase
        <N extends Arc, NB extends ArcPeerBase<N, NB, NM>, NM extends ArcPeerMixin<N, NB, NM>>

        extends ShapePeerBase<N, NB, NM> {

    @Override
    public void bind(N a, SceneRequester sceneRequester) {
        super.bind(a, sceneRequester);
        requestUpdateOnPropertiesChange(sceneRequester
                , a.typeProperty()
                , a.centerXProperty()
                , a.centerYProperty()
                , a.radiusXProperty()
                , a.radiusYProperty()
                , a.startAngleProperty()
                , a.lengthProperty()
        );
    }

    @Override
    public boolean updateProperty(ObservableValue changedProperty) {
        N c = node;
        return super.updateProperty(changedProperty)
                || updateProperty(c.typeProperty(), changedProperty, p -> mixin.updateType(p))
                || updateProperty(c.centerXProperty(), changedProperty, p -> mixin.updateCenterX(p.doubleValue()))
                || updateProperty(c.centerYProperty(), changedProperty, p -> mixin.updateCenterY(p.doubleValue()))
                || updateProperty(c.radiusXProperty(), changedProperty, p-> mixin.updateRadiusX(p.doubleValue()))
                || updateProperty(c.radiusYProperty(), changedProperty, p-> mixin.updateRadiusY(p.doubleValue()))
                || updateProperty(c.startAngleProperty(), changedProperty, p-> mixin.updateStartAngle(p.doubleValue()))
                || updateProperty(c.lengthProperty(), changedProperty, p-> mixin.updateLength(p.doubleValue()))
                ;
    }
}
