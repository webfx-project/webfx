package naga.fx.spi.peer.base;

import javafx.beans.value.ObservableValue;
import naga.fx.scene.SceneRequester;
import javafx.scene.shape.Rectangle;

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
                || updateProperty(r.xProperty(), changedProperty, p -> mixin.updateX(p.doubleValue()))
                || updateProperty(r.yProperty(), changedProperty, p -> mixin.updateY(p.doubleValue()))
                || updateProperty(r.widthProperty(), changedProperty, p -> mixin.updateWidth(p.doubleValue()))
                || updateProperty(r.heightProperty(), changedProperty, p -> mixin.updateHeight(p.doubleValue()))
                || updateProperty(r.arcWidthProperty(), changedProperty, p -> mixin.updateArcWidth(p.doubleValue()))
                || updateProperty(r.arcHeightProperty(), changedProperty, p -> mixin.updateArcHeight(p.doubleValue()))
                ;
    }
}
