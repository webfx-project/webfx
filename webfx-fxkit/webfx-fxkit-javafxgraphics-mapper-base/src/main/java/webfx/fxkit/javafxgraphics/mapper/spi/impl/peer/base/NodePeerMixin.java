package webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.base;

import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.Effect;
import javafx.scene.transform.Transform;
import webfx.fxkit.javafxgraphics.mapper.spi.NodePeer;
import webfx.fxkit.javafxgraphics.mapper.spi.SceneRequester;

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
    default D getNode() {
        return getNodePeerBase().getNode();
    }

    @Override
    default void requestFocus() {
        getNodePeerBase().requestFocus();
    }

    @Override
    default boolean isTreeVisible() {
        return getNodePeerBase().isTreeVisible();
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

    void updateId(String id);

    void updateVisible(Boolean visible);

    void updateOpacity(Double opacity);

    void updateClip(Node clip);

    void updateBlendMode(BlendMode blendMode);

    void updateEffect(Effect effect);

    /*default*/ void updateLayoutX(Number layoutX); /*{
        updateLocalToParentTransforms(getNodePeerBase().getNode().localToParentTransforms());
    }*/

    /*default*/ void updateLayoutY(Number layoutY); /*{
        updateLocalToParentTransforms(getNodePeerBase().getNode().localToParentTransforms());
    }*/

    /*default*/ void updateTransforms(List<Transform> transforms, ListChangeListener.Change<Transform> change); /*{
        updateLocalToParentTransforms(getNodePeerBase().getNode().localToParentTransforms());
    }*/

    void updateLocalToParentTransforms(List<Transform> localToParentTransforms);

    void updateDisabled(Boolean disabled);

    void updateStyleClass(List<String> styleClass, ListChangeListener.Change<String> change);

    void updateCursor(Cursor cursor);
}
