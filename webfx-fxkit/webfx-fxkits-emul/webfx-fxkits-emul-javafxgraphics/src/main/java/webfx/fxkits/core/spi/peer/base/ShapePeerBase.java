package webfx.fxkits.core.spi.peer.base;

import emul.javafx.beans.value.ObservableValue;
import webfx.fxkits.core.scene.SceneRequester;
import emul.javafx.scene.shape.Shape;

/**
 * @author Bruno Salmon
 */
public abstract class ShapePeerBase
        <N extends Shape, NB extends ShapePeerBase<N, NB, NM>, NM extends ShapePeerMixin<N, NB, NM>>

        extends NodePeerBase<N, NB, NM> {

    @Override
    public void bind(N shape, SceneRequester sceneRequester) {
        super.bind(shape, sceneRequester);
        requestUpdateOnPropertiesChange(sceneRequester
                , shape.fillProperty()
                , shape.smoothProperty()
                , shape.strokeProperty()
                , shape.strokeTypeProperty()
                , shape.strokeWidthProperty()
                , shape.strokeLineCapProperty()
                , shape.strokeLineJoinProperty()
                , shape.strokeMiterLimitProperty()
                , shape.strokeDashOffsetProperty()
        );
    }

    @Override
    public boolean updateProperty(ObservableValue changedProperty) {
        N s = node;
        mixin.updateStrokeDashArray(s.getStrokeDashArray());
        return super.updateProperty(changedProperty)
                || updateProperty(s.fillProperty(), changedProperty, mixin::updateFill)
                || updateProperty(s.smoothProperty(), changedProperty, mixin::updateSmooth)
                || updateProperty(s.strokeProperty(), changedProperty, mixin::updateStroke)
                || updateProperty(s.strokeTypeProperty(), changedProperty, mixin::updateStrokeType)
                || updateProperty(s.strokeWidthProperty(), changedProperty, mixin::updateStrokeWidth)
                || updateProperty(s.strokeLineCapProperty(), changedProperty, mixin::updateStrokeLineCap)
                || updateProperty(s.strokeLineJoinProperty(), changedProperty, mixin::updateStrokeLineJoin)
                || updateProperty(s.strokeMiterLimitProperty(), changedProperty, mixin::updateStrokeMiterLimit)
                || updateProperty(s.strokeDashOffsetProperty(), changedProperty, mixin::updateStrokeDashOffset)
                ;
    }
}
