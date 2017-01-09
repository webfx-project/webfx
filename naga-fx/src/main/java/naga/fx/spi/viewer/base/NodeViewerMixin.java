package naga.fx.spi.viewer.base;

import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import naga.fx.scene.Node;
import naga.fx.scene.SceneRequester;
import naga.fx.scene.effect.BlendMode;
import naga.fx.scene.effect.Effect;
import naga.fx.scene.transform.Transform;
import naga.fx.spi.viewer.NodeViewer;

import java.util.Collection;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public interface NodeViewerMixin
        <D extends Node, DV extends NodeViewerBase<D, DV, DM>, DM extends NodeViewerMixin<D, DV, DM>>
        extends NodeViewer<D> {

    DV getNodeViewerBase();

    @Override
    default void bind(D node, SceneRequester sceneRequester) {
        getNodeViewerBase().bind(node, sceneRequester);
    }

    @Override
    default void unbind() {
        getNodeViewerBase().unbind();
    }

    @Override
    default boolean updateProperty(ObservableValue changedProperty) {
        return getNodeViewerBase().updateProperty(changedProperty);
    }

    @Override
    default boolean updateList(ObservableList changedList) {
        return getNodeViewerBase().updateList(changedList);
    }

    void updateMouseTransparent(Boolean mouseTransparent);

    void updateVisible(Boolean visible);

    void updateOpacity(Double opacity);

    void updateClip(Node clip);

    void updateBlendMode(BlendMode blendMode);

    void updateEffect(Effect effect);

    default void updateLayoutX(Double layoutX) {
        updateLocalToParentTransforms(getNodeViewerBase().getNode().localToParentTransforms());
    }

    default void updateLayoutY(Double layoutY) {
        updateLocalToParentTransforms(getNodeViewerBase().getNode().localToParentTransforms());
    }

    default void updateTransforms(List<Transform> transforms) {
        updateLocalToParentTransforms(getNodeViewerBase().getNode().localToParentTransforms());
    }

    void updateLocalToParentTransforms(Collection<Transform> localToParentTransforms);
}
