package webfx.fxkit.mapper.spi.impl.peer.javafxgraphics;

import javafx.beans.value.ObservableValue;
import webfx.fxkit.mapper.spi.SceneRequester;
import javafx.scene.shape.Line;

/**
 * @author Bruno Salmon
 */
public class LinePeerBase
        <N extends Line, NB extends LinePeerBase<N, NB, NM>, NM extends LinePeerMixin<N, NB, NM>>

        extends ShapePeerBase<N, NB, NM> {

    @Override
    public void bind(N c, SceneRequester sceneRequester) {
        super.bind(c, sceneRequester);
        requestUpdateOnPropertiesChange(sceneRequester
                , c.startXProperty()
                , c.startYProperty()
                , c.endXProperty()
                , c.endYProperty()
        );
    }

    @Override
    public boolean updateProperty(ObservableValue changedProperty) {
        N c = node;
        return super.updateProperty(changedProperty)
                || updateProperty(c.startXProperty(), changedProperty, p -> mixin.updateStartX(p.doubleValue()))
                || updateProperty(c.startYProperty(), changedProperty, p -> mixin.updateStartY(p.doubleValue()))
                || updateProperty(c.endXProperty(), changedProperty, p -> mixin.updateEndX(p.doubleValue()))
                || updateProperty(c.endYProperty(), changedProperty, p -> mixin.updateEndY(p.doubleValue()))
                ;
    }
}
