package webfx.fxkits.core.spi.peer.base;

import emul.javafx.beans.value.ObservableValue;
import webfx.fxkits.core.scene.SceneRequester;
import emul.javafx.scene.shape.Rectangle;

/**
 * @author Bruno Salmon
 */
public class RectanglePeerBase
        <N extends Rectangle, NB extends RectanglePeerBase<N, NB, NM>, NM extends RectanglePeerMixin<N, NB, NM>>

        extends ShapePeerBase<N, NB, NM> {

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
