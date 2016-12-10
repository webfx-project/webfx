package naga.toolkit.fx.spi.viewer.base;

import javafx.beans.property.Property;
import javafx.collections.ObservableList;
import naga.toolkit.fx.scene.effect.BlendMode;
import naga.toolkit.fx.scene.Node;
import naga.toolkit.fx.spi.DrawingRequester;
import naga.toolkit.fx.spi.viewer.NodeViewer;
import naga.toolkit.fx.scene.effect.Effect;
import naga.toolkit.spi.events.MouseEvent;
import naga.toolkit.spi.events.UiEventHandler;
import naga.toolkit.fx.scene.transform.Transform;

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
    default void bind(D node, DrawingRequester drawingRequester) {
        getNodeViewerBase().bind(node, drawingRequester);
    }

    @Override
    default void unbind() {
        getNodeViewerBase().unbind();
    }

    @Override
    default boolean updateProperty(Property changedProperty) {
        return getNodeViewerBase().updateProperty(changedProperty);
    }

    @Override
    default boolean updateList(ObservableList changedList) {
        return getNodeViewerBase().updateList(changedList);
    }

    void updateOnMouseClicked(UiEventHandler<? super MouseEvent> onMouseClicked);

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
