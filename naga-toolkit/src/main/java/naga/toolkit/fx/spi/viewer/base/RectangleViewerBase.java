package naga.toolkit.fx.spi.viewer.base;

import javafx.beans.value.ObservableValue;
import naga.toolkit.fx.scene.shape.Rectangle;
import naga.toolkit.fx.scene.SceneRequester;
import naga.toolkit.fx.spi.viewer.RectangleViewer;

/**
 * @author Bruno Salmon
 */
public class RectangleViewerBase
        extends ShapeViewerBase<Rectangle, RectangleViewerBase, RectangleViewerMixin>
        implements RectangleViewer {

    @Override
    public void bind(Rectangle r, SceneRequester sceneRequester) {
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
        Rectangle r = node;
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
