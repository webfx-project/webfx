package naga.toolkit.fx.spi.viewer.base;

import javafx.beans.value.ObservableValue;
import naga.toolkit.fx.scene.SceneRequester;
import naga.toolkit.fx.scene.shape.Shape;

/**
 * @author Bruno Salmon
 */
public abstract class ShapeViewerBase
        <N extends Shape, NB extends ShapeViewerBase<N, NB, NM>, NM extends ShapeViewerMixin<N, NB, NM>>

        extends NodeViewerBase<N, NB, NM> {

    @Override
    public void bind(N shape, SceneRequester sceneRequester) {
        super.bind(shape, sceneRequester);
        requestUpdateOnPropertiesChange(sceneRequester
                , shape.fillProperty()
                , shape.smoothProperty()
                , shape.strokeProperty()
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
                || updateProperty(s.strokeWidthProperty(), changedProperty, mixin::updateStrokeWidth)
                || updateProperty(s.strokeLineCapProperty(), changedProperty, mixin::updateStrokeLineCap)
                || updateProperty(s.strokeLineJoinProperty(), changedProperty, mixin::updateStrokeLineJoin)
                || updateProperty(s.strokeMiterLimitProperty(), changedProperty, mixin::updateStrokeMiterLimit)
                || updateProperty(s.strokeDashOffsetProperty(), changedProperty, mixin::updateStrokeDashOffset)
                ;
    }
}
