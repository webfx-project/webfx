package naga.toolkit.fx.spi.viewer.base;

import javafx.beans.value.ObservableValue;
import naga.toolkit.fx.scene.SceneRequester;
import naga.toolkit.fx.scene.shape.Rectangle;

/**
 * @author Bruno Salmon
 */
public class RectangleViewerBase
        <N extends Rectangle, NB extends RectangleViewerBase<N, NB, NM>, NM extends RectangleViewerMixin<N, NB, NM>>

        extends ShapeViewerBase<N, NB, NM> {

    @Override
    public void bind(N r, SceneRequester sceneRequester) {
        super.bind(r, sceneRequester);
        requestUpdateOnPropertiesChange(sceneRequester
                , r.xProperty()
                , r.yProperty()
                , r.widthProperty()
                , r.heightProperty()
                , r.arcWidthProperty()
                , r.arcHeightProperty()
        );
    }

    @Override
    public boolean updateProperty(ObservableValue changedProperty) {
        N r = node;
        return super.updateProperty(changedProperty)
                || updateProperty(r.xProperty(), changedProperty, mixin::updateX)
                || updateProperty(r.yProperty(), changedProperty, mixin::updateY)
                || updateProperty(r.widthProperty(), changedProperty, mixin::updateWidth)
                || updateProperty(r.heightProperty(), changedProperty, mixin::updateHeight)
                || updateProperty(r.arcWidthProperty(), changedProperty, mixin::updateArcWidth)
                || updateProperty(r.arcHeightProperty(), changedProperty, mixin::updateArcHeight)
                ;
    }
}
