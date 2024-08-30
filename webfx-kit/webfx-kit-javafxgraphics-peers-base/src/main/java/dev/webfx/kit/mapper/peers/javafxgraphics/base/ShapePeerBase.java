package dev.webfx.kit.mapper.peers.javafxgraphics.base;

import javafx.beans.value.ObservableValue;
import dev.webfx.kit.mapper.peers.javafxgraphics.SceneRequester;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.shape.Shape;

/**
 * @author Bruno Salmon
 */
public class ShapePeerBase
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
        requestUpdateOnListChange(sceneRequester, shape.getStrokeDashArray());
    }

    @Override
    public boolean updateProperty(ObservableValue changedProperty) {
        N s = node;
        return super.updateProperty(changedProperty)
               || updateProperty(s.fillProperty(), changedProperty, mixin::updateFill)
               || updateProperty(s.smoothProperty(), changedProperty, mixin::updateSmooth)
               || updateProperty(s.strokeProperty(), changedProperty, mixin::updateStroke)
               || updateProperty(s.strokeTypeProperty(), changedProperty, mixin::updateStrokeType)
               || updateProperty(s.strokeWidthProperty(), changedProperty, p -> mixin.updateStrokeWidth(p.doubleValue()))
               || updateProperty(s.strokeLineCapProperty(), changedProperty, mixin::updateStrokeLineCap)
               || updateProperty(s.strokeLineJoinProperty(), changedProperty, mixin::updateStrokeLineJoin)
               || updateProperty(s.strokeMiterLimitProperty(), changedProperty, p -> mixin.updateStrokeMiterLimit(p.doubleValue()))
               || updateProperty(s.strokeDashOffsetProperty(), changedProperty, p -> mixin.updateStrokeDashOffset(p.doubleValue()))
                ;
    }

    @Override
    public boolean updateList(ObservableList list, ListChangeListener.Change change) {
        return super.updateList(list, change) ||
               updateList2(node.getStrokeDashArray(), list, mixin::updateStrokeDashArray)
                ;
    }
}
