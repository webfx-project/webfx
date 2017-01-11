package naga.fx.spi.peer.base;

import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import naga.fx.scene.Node;
import naga.fx.scene.SceneRequester;
import naga.fx.scene.effect.BlendMode;
import naga.fx.scene.effect.Effect;
import naga.fx.scene.transform.Transform;
import naga.fx.spi.peer.NodePeer;

import java.util.Collection;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public interface NodePeerMixin
        <D extends Node, DV extends NodePeerBase<D, DV, DM>, DM extends NodePeerMixin<D, DV, DM>>
        extends NodePeer<D> {

    DV getNodePeerBase();

    @Override
    default void bind(D node, SceneRequester sceneRequester) {
        getNodePeerBase().bind(node, sceneRequester);
    }

    @Override
    default void unbind() {
        getNodePeerBase().unbind();
    }

    @Override
    default boolean updateProperty(ObservableValue changedProperty) {
        return getNodePeerBase().updateProperty(changedProperty);
    }

    @Override
    default boolean updateList(ObservableList changedList) {
        return getNodePeerBase().updateList(changedList);
    }

    void updateMouseTransparent(Boolean mouseTransparent);

    void updateVisible(Boolean visible);

    void updateOpacity(Double opacity);

    void updateClip(Node clip);

    void updateBlendMode(BlendMode blendMode);

    void updateEffect(Effect effect);

    default void updateLayoutX(Double layoutX) {
        updateLocalToParentTransforms(getNodePeerBase().getNode().localToParentTransforms());
    }

    default void updateLayoutY(Double layoutY) {
        updateLocalToParentTransforms(getNodePeerBase().getNode().localToParentTransforms());
    }

    default void updateTransforms(List<Transform> transforms) {
        updateLocalToParentTransforms(getNodePeerBase().getNode().localToParentTransforms());
    }

    void updateLocalToParentTransforms(Collection<Transform> localToParentTransforms);

    void updateDisabled(Boolean disabled);
}
