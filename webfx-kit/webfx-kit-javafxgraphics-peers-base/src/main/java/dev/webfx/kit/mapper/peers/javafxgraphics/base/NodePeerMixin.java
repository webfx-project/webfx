package dev.webfx.kit.mapper.peers.javafxgraphics.base;

import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.Effect;
import javafx.scene.image.WritableImage;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.transform.Transform;
import dev.webfx.kit.mapper.peers.javafxgraphics.NodePeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.SceneRequester;

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

    void updateLayoutX(Number layoutX);

    void updateLayoutY(Number layoutY);

    void updateTranslateX(Number translateX);

    void updateTranslateY(Number translateY);

    void updateScaleX(Number scaleX);

    void updateScaleY(Number scaleX);

    void updateRotate(Number rotate);

    void updateTransforms(List<Transform> transforms, ListChangeListener.Change<Transform> change);

    void updateAllNodeTransforms(List<Transform> localToParentTransforms);

    void updateDisabled(Boolean disabled);

    void updateStyleClass(List<String> styleClass, ListChangeListener.Change<String> change);

    void updateCursor(Cursor cursor);

    void updateOnDragDetected(EventHandler<? super MouseEvent> eventHandler);

    void updateOnDragEntered(EventHandler<? super DragEvent> eventHandler);

    void updateOnDragOver(EventHandler<? super DragEvent> eventHandler);

    void updateOnDragDropped(EventHandler<? super DragEvent> eventHandler);

    void updateOnDragExited(EventHandler<? super DragEvent> eventHandler);

    void updateOnDragDone(EventHandler<? super DragEvent> eventHandler);

    void updateStyle(String style);

    @Override
    default WritableImage snapshot(SnapshotParameters params, WritableImage image) {
        return getNodePeerBase().snapshot(params, image);
    }
}
