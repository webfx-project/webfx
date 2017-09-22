package naga.fx.spi.peer.base;

import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.Effect;
import javafx.scene.transform.Transform;
import naga.fx.scene.SceneRequester;
import naga.fx.spi.peer.NodePeer;

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
    default boolean updateList(ObservableList changedList, ListChangeListener.Change change) {
        return getNodePeerBase().updateList(changedList, change);
    }

    void updateMouseTransparent(Boolean mouseTransparent);

    void updateVisible(Boolean visible);

    void updateOpacity(Double opacity);

    void updateClip(Node clip);

    void updateBlendMode(BlendMode blendMode);

    void updateEffect(Effect effect);

    void updateLayoutX(Double layoutX);

    void updateLayoutY(Double layoutY);

    void updateTransforms(List<Transform> transforms, ListChangeListener.Change<Transform> change);

    void updateLocalToParentTransforms(List<Transform> localToParentTransforms);

    void updateDisabled(Boolean disabled);

    void updateStyleClass(List<String> styleClass, ListChangeListener.Change<String> change);
}
