package naga.fx.spi.viewer.base;

import javafx.beans.value.ObservableValue;
import naga.fx.scene.SceneRequester;
import naga.fx.scene.shape.Line;

/**
 * @author Bruno Salmon
 */
public class LineViewerBase
        <N extends Line, NB extends LineViewerBase<N, NB, NM>, NM extends LineViewerMixin<N, NB, NM>>

        extends ShapeViewerBase<N, NB, NM> {

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
                || updateProperty(c.startXProperty(), changedProperty, mixin::updateStartX)
                || updateProperty(c.startYProperty(), changedProperty, mixin::updateStartY)
                || updateProperty(c.endXProperty(), changedProperty, mixin::updateEndX)
                || updateProperty(c.endYProperty(), changedProperty, mixin::updateEndY)
                ;
    }
}
